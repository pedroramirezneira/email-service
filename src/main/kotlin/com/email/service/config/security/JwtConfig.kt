package com.email.service.config.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtConfig(
    private val properties: SecurityProperties
) {
    @Bean
    fun encoder(): JwtEncoder {
        val jwk = OctetSequenceKey.Builder(properties.jwtSecret.toByteArray(Charsets.UTF_8))
            .algorithm(JWSAlgorithm.HS256)
            .keyID("hs256-key-1")
            .build()
        val jwkSet = ImmutableJWKSet<SecurityContext>(JWKSet(jwk))
        return NimbusJwtEncoder(jwkSet)
    }

    @Bean
    fun decoder(): JwtDecoder {
        val key = SecretKeySpec(properties.jwtSecret.toByteArray(Charsets.UTF_8), "HmacSHA256")
        return NimbusJwtDecoder.withSecretKey(key)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
    }

    @Bean
    fun authConverter(): Converter<Jwt, AbstractAuthenticationToken> {
        val converter = JwtGrantedAuthoritiesConverter().apply {
            setAuthoritiesClaimName("roles")
            setAuthorityPrefix("ROLE_")
        }
        return Converter { jwt ->
            val authorities = converter.convert(jwt) ?: emptyList()
            JwtAuthenticationToken(jwt, authorities, jwt.subject)
        }
    }
}
