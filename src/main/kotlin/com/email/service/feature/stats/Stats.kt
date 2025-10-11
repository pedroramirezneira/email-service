package com.email.service.feature.stats

import com.email.service.feature.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant
import java.util.*

@Entity
data class Stats(
    @Id val id: UUID = UUID.randomUUID(),

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now(),
)
