package com.email.service.feature.email

import com.email.service.common.Result
import com.email.service.config.email.EmailProperties
import com.email.service.feature.email.provider.EmailProviderRegistry
import com.email.service.feature.email.provider.EmailProviderStrategy
import com.email.service.feature.stats.StatsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailService(
    private val registry: EmailProviderRegistry,
    private val properties: EmailProperties,
    private val statsService: StatsService,
) {
    fun sendEmail(userId: UUID, email: EmailDto): Result<Unit, EmailFailure> {
        val strategy = registry.getDefaultStrategy()
            ?: return Result.Failure(EmailFailure.NoProviderAvailable)
        val result = sendEmail(email, strategy, 0)
        if (result is Result.Success) {
            statsService.addStats(userId)
        }
        return result
    }

    private tailrec fun sendEmail(
        email: EmailDto,
        strategy: EmailProviderStrategy,
        attempt: Int,
    ): Result<Unit, EmailFailure> {
        if (attempt >= properties.maxRetries) {
            return Result.Failure(EmailFailure.MaxRetriesExceeded)
        }
        val result = strategy.send(email)
        if (result is Result.Failure && result.error is EmailFailure.ProviderError) {
            val nextStrategy = registry.getNextStrategy(strategy)
                ?: return result
            if (nextStrategy == strategy) {
                return result
            }
            return sendEmail(email, nextStrategy, attempt + 1)
        }
        return result
    }
}
