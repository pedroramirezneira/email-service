package com.email.service.config.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security")
data class SecurityProperties(
    val jwtSecret: String,
    val jwtExpirationMs: Long,
    val issuer: String,
)
