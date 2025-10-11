package com.email.service.feature.role

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRoleRepository : JpaRepository<UserRole, UUID> {
    fun findByUserId(userId: UUID): List<UserRole>
}
