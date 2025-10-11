package com.email.service.feature.stats

import com.email.service.feature.user.UserRepository
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Service
class StatsService(
    private val repository: StatsRepository,
    private val userRepository: UserRepository,
) {
    private val zone = ZoneId.of("America/Argentina/Buenos_Aires")

    fun getStats(): List<StatsDto> {
        val now = ZonedDateTime.now(zone)
        val start = now.toLocalDate().atStartOfDay(zone).toInstant()
        val end = now.toLocalDate().plusDays(1).atStartOfDay(zone).toInstant()
        return repository.countEmailsByUserInRange(start, end)
    }

    fun addStats(userId: UUID) {
        val user = userRepository.findById(userId)
        if (user.isEmpty) return
        val stats = Stats(user = user.get())
        repository.save(stats)
    }
}
