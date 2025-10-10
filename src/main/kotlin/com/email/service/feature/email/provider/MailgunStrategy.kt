package com.email.service.feature.email.provider

import com.email.service.common.Result
import com.email.service.config.email.EmailProperties
import com.email.service.config.email.provider.MailgunProperties
import com.email.service.feature.email.EmailDto
import com.email.service.feature.email.EmailFailure
import com.mailgun.api.v3.MailgunMessagesApi
import com.mailgun.client.MailgunClient
import com.mailgun.model.message.Message
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@ConditionalOnProperty(name = ["email.enabled-providers"], havingValue = "mailgun")
@Service
class MailgunStrategy(
    val mailgunProperties: MailgunProperties,
    val emailProperties: EmailProperties,
) : EmailProviderStrategy {
    val client = MailgunClient.config(mailgunProperties.apiKey).createApi(MailgunMessagesApi::class.java)

    override val provider = EmailProvider.MAILGUN

    override fun send(email: EmailDto): Result<Unit, EmailFailure> {
        return try {
            val message = Message.builder()
                .from("${emailProperties.fromName} <${emailProperties.fromAddress}>")
                .to(email.to)
                .subject(email.subject)
                .text(email.body)
                .build()
            val response = client.sendMessage(mailgunProperties.domain, message)
            response.message
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(EmailFailure.ProviderError("Mailgun error: ${e.message}"))
        }
    }
}
