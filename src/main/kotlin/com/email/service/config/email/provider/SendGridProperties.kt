package com.email.service.config.email.provider

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "email.sendgrid")
data class SendGridProperties(
    val apiKey: String?,
)
