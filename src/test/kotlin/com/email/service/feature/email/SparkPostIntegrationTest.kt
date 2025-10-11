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
        "email.default-provider=sparkpost",
        "email.sparkpost.enabled=true",
    ]
)
class SparkPostIntegrationTest(
    @Autowired val service: EmailService,
) {
    @Test
    fun testSparkPost() {
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
