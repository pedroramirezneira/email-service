package com.email.service.config.email.provider

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix =  "email.sparkpost")
data class SparkPostProperties(
    val apiKey: String?,
)
