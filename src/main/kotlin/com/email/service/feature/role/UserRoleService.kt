package com.email.service.feature.role

import com.email.service.feature.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserRoleService(
    private val repository: UserRoleRepository,
    private val userRepository: UserRepository,
) {
    fun assignRoleToUser(userId: UUID, role: Role) {
        val user = userRepository.findById(userId)
        if (user.isEmpty) return
        val userRole = UserRole(user = user.get(), role = role)
        repository.save(userRole)
    }

    fun getRolesForUser(userId: UUID): List<Role> {
        return repository.findByUserId(userId).map { it.role }
    }
}
