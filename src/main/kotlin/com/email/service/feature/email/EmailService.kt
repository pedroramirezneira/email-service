package com.email.service.feature.email

import com.email.service.common.Result
import com.email.service.feature.email.provider.EmailProviderRegistry
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val registry: EmailProviderRegistry
) {
    fun sendEmail(email: EmailDto): Result<Unit, EmailFailure> {
        val strategy = registry.getDefaultStrategy()
        return strategy?.send(email) ?: Result.Failure(EmailFailure.NoProviderAvailable)
    }
}
