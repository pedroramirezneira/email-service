package com.email.service.config.email

import com.email.service.feature.email.provider.EmailProvider
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "email")
data class EmailProperties(
    val enabledProviders: List<EmailProvider>,
    val defaultProvider: EmailProvider,
    val fromAddress: String,
    val fromName: String?,
)
