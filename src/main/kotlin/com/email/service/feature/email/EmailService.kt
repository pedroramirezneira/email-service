package com.email.service.feature.email

import com.email.service.common.Result
import com.email.service.config.email.EmailProperties
import com.email.service.feature.email.provider.EmailProviderRegistry
import com.email.service.feature.email.provider.EmailProviderStrategy
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val registry: EmailProviderRegistry,
    private val properties: EmailProperties,
) {
    fun sendEmail(email: EmailDto): Result<Unit, EmailFailure> {
        val strategy = registry.getDefaultStrategy()
            ?: return Result.Failure(EmailFailure.NoProviderAvailable)
        return sendEmail(email, strategy, 0)
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
