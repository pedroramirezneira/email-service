package com.email.service.feature.email

data class EmailDto(
    val to: List<String>,
    val cc: List<String> = emptyList(),
    val bcc: List<String> = emptyList(),
    val subject: String = "(No Subject)",
    val body: String
)
