package com.email.service.feature.email

import com.email.service.common.Result
import com.email.service.config.email.EmailProperties
import com.email.service.feature.auth.AuthService
import com.email.service.feature.auth.RegisterDto
import com.email.service.feature.email.provider.EmailProviderRegistry
import com.email.service.feature.stats.StatsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test

@SpringBootTest
@TestPropertySource(
    properties = [
        "email.sparkpost.enabled=false",
        "email.sparkpost.enabled=false",
        "email.mailgun.enabled=false",
    ]
)
class LimitExceededTest(
    @Autowired private val properties: EmailProperties,
    @Autowired statsService: StatsService,
    @Autowired private val authService: AuthService,
) {
    private val registry = EmailProviderRegistry(listOf(MockEmailStrategy()), properties)

    private val service = EmailService(registry, properties, statsService)

    @Test
    fun testLimitExceeded() {
        val result = authService.register(
            RegisterDto(
                username = "test",
                password = "test123",
            )
        )
        assert(result is Result.Success)
        val user = (result as Result.Success).value
        val email = EmailDto(
            to = listOf("test@mail.com"),
            body = "Test email body",
        )
        for (i in 0 until properties.dailyLimit) {
            val final = service.sendEmail(user.id, email)
            assert(final is Result.Success)
        }
        val final = service.sendEmail(user.id, email)
        assert(final is Result.Failure)
        assert((final as Result.Failure).error is EmailFailure.LimitExceeded)
    }
}
