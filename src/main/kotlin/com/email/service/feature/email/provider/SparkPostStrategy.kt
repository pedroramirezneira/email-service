package com.email.service.feature.email.provider

import com.email.service.common.Result
import com.email.service.config.email.EmailProperties
import com.email.service.config.email.provider.SparkPostProperties
import com.email.service.feature.email.EmailDto
import com.email.service.feature.email.EmailFailure
import com.sparkpost.Client
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@ConditionalOnProperty(name = ["email.sparkpost.enabled"], havingValue = "true")
@Service
class SparkPostStrategy(
    sparkPostProperties: SparkPostProperties,
    private val emailProperties: EmailProperties,
) : EmailProviderStrategy {
    private val client = Client(sparkPostProperties.apiKey)

    override val provider = EmailProvider.SPARKPOST

    override fun send(email: EmailDto): Result<Unit, EmailFailure> {
        return try {
            val response = client.sendMessage(
                "${emailProperties.fromName} <${emailProperties.fromAddress}>",
                email.to,
                email.subject,
                email.body,
                null
            )
            when (response.responseCode) {
                in 200..299 -> Result.Success(Unit)
                else -> Result.Failure(
                    EmailFailure.ProviderError(
                        "SparkPost error: ${response.responseCode} - ${response.responseBody}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.Failure(EmailFailure.ProviderError("SparkPost error: ${e.message}"))
        }
    }
}
