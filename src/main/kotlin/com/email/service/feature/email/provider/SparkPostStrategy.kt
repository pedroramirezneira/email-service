package com.email.service.feature.email.provider

import com.email.service.common.Result
import com.email.service.config.email.EmailProperties
import com.email.service.config.email.provider.SparkPostProperties
import com.email.service.feature.email.EmailDto
import com.email.service.feature.email.EmailFailure
import com.sparkpost.Client
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@ConditionalOnProperty(name = ["email.enabled-providers"], havingValue = "sparkpost")
@Service
class SparkPostStrategy(
    sparkPostProperties: SparkPostProperties,
    val emailProperties: EmailProperties,
) : EmailProviderStrategy {
    val client = Client(sparkPostProperties.apiKey)

    override val provider = EmailProvider.SPARKPOST

    override fun send(email: EmailDto): Result<Unit, EmailFailure> {
        val response = client.sendMessage(
            "${emailProperties.fromName} <${emailProperties.fromAddress}>",
            email.to,
            email.subject,
            email.body,
            null
        )
        return when (response.responseCode) {
            200, 201 -> Result.Success(Unit)
            else -> Result.Failure(
                EmailFailure.ProviderError(
                    "SparkPost error: ${response.responseCode} - ${response.responseBody}"
                )
            )
        }
    }
}
