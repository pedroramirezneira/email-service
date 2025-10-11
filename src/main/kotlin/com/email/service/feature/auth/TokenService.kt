package com.email.service.feature.auth

import com.email.service.common.Result
import com.email.service.config.security.SecurityProperties
import com.email.service.feature.role.Role
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TokenService(
    private val jwtEncoder: JwtEncoder,
    private val properties: SecurityProperties
) {
    fun issueAccessToken(subject: String, roles: Set<Role>): Result<Jwt, Unit> {
        return try {
            val now = Instant.now()
            val claims = JwtClaimsSet.builder()
                .issuer(properties.issuer)
                .issuedAt(now)
                .expiresAt(now.plusMillis(properties.jwtExpirationMs))
                .subject(subject)
                .claim("roles", roles.map { it.name })
                .build()
            val header = JwsHeader.with(MacAlgorithm.HS256).build()
            Result.Success(jwtEncoder.encode(JwtEncoderParameters.from(header, claims)))
        } catch (e: Exception) {
            Result.Failure(Unit)
        }
    }
}
