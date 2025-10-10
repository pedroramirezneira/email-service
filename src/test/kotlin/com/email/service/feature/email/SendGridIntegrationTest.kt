package com.email.service.feature.email

import com.email.service.common.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test

@SpringBootTest
@TestPropertySource(
    properties = [
        "email.default-provider=sendgrid",
        "email.enabled-providers=sendgrid",
    ]
)
class SendGridIntegrationTest(
    @Autowired val service: EmailService,
) {
    @Test
    fun testSendGrid() {
        val result = service.sendEmail(
            EmailDto(
                to = listOf("test@mail.com"),
                body = "Test email body",
            )
        )
        assert(result is Result.Success)
    }
}
