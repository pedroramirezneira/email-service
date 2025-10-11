package com.email.service.feature.stats

import java.util.*

data class StatsDto(
    val userId: UUID,
    val username: String,
    val total: Long,
)
