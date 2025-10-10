package com.email.service.config.email.provider

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "email.mailgun")
data class MailgunProperties(
    val apiKey: String?,
    val domain: String?,
)
