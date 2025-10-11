package com.email.service.feature.email.provider

import com.email.service.common.Result
import com.email.service.config.email.EmailProperties
import com.email.service.config.email.provider.SendGridProperties
import com.email.service.feature.email.EmailDto
import com.email.service.feature.email.EmailFailure
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@ConditionalOnProperty(name = ["email.sendgrid.enabled"], havingValue = "true")
@Service
class SendGridStrategy(
    sendGridProperties: SendGridProperties,
    emailProperties: EmailProperties,
) : EmailProviderStrategy {
    val sendGrid = SendGrid(sendGridProperties.apiKey)
    val from = Email(emailProperties.fromAddress, emailProperties.fromName)

    override val provider = EmailProvider.SENDGRID

    override fun send(email: EmailDto): Result<Unit, EmailFailure> {
        val to = Email(email.to.firstOrNull())
        val content = Content("text/plain", email.body)
        val mail = Mail(from, email.subject, to, content)
        val personalization = Personalization()
        email.to.forEach {
            personalization.addTo(Email(it))
        }
        mail.addPersonalization(personalization)
        val request = Request().apply {
            method = Method.POST
            endpoint = "mail/send"
            body = mail.build()
        }
        return try {
            val response = sendGrid.api(request)
            when (response.statusCode) {
                in 200..299 -> Result.Success(Unit)
                else -> Result.Failure(
                    EmailFailure.ProviderError(
                        "SendGrid error: ${response.statusCode} - ${response.body}"
                    )
                )
            }
        } catch (e: Exception) {
            Result.Failure(EmailFailure.ProviderError("SendGrid error: ${e.message}"))
        }
    }
}
