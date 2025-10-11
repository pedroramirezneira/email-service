package com.email.service.feature.stats

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface StatsRepository : JpaRepository<Stats, UUID> {
    @Query(
        """
    SELECT s.user.id   AS userId,
           s.user.username AS username,
           COUNT(s.id) AS total
    FROM Stats s
    WHERE s.timestamp >= :start AND s.timestamp < :end
    GROUP BY s.user.id, s.user.username
    ORDER BY COUNT(s.id) DESC
    """
    )
    fun countEmailsByUserInRange(
        @Param("start") start: Instant,
        @Param("end") end: Instant
    ): List<StatsDto>
}
