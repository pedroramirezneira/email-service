package com.email.service.feature.email

import com.email.service.common.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.util.*
import kotlin.test.Test

@SpringBootTest
@TestPropertySource(
    properties = [
        "email.default-provider=mailgun",
        "email.mailgun.enabled=true",
    ]
)
class MailgunIntegrationTest(
    @Autowired val service: EmailService,
) {
    @Test
    fun testMailgun() {
        val result = service.sendEmail(
            UUID.randomUUID(),
            EmailDto(
                to = listOf("test@mail.com"),
                body = "Test email body",
            )
        )
        assert(result is Result.Success)
    }
}
