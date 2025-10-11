package com.email.service.feature.email

import com.email.service.common.Result
import com.email.service.feature.auth.TokenService
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidationException
import org.springframework.test.context.TestPropertySource
import java.lang.Thread.sleep
import java.util.UUID
import kotlin.test.Test

@SpringBootTest
@TestPropertySource(
    properties = [
        "security.jwt-expiration-ms=1000",
        "security.jwt-secret=test-secret-key-test-secret-key-test-secret-key",
        "security.issuer=test-issuer",
    ]
)
class TokenExpirationTest(
    @Autowired private val tokenService: TokenService,
    @Autowired private val jwtDecoder: JwtDecoder,
) {
    @Test
    fun testTokenExpiration() {
        val token = tokenService.issueAccessToken(
            UUID.randomUUID().toString(),
            emptySet()
        )
        assert(token is Result.Success)
        jwtDecoder.decode((token as Result.Success).value.tokenValue)
        sleep(1500)
        assertThrows<JwtValidationException> {
            jwtDecoder.decode(token.value.tokenValue)
        }
    }
}
