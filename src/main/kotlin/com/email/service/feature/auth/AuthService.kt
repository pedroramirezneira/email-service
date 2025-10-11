package com.email.service.feature.auth

import com.email.service.common.Result
import com.email.service.feature.role.Role
import com.email.service.feature.role.UserRoleService
import com.email.service.feature.user.User
import com.email.service.feature.user.UserDto
import com.email.service.feature.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val encoder: PasswordEncoder,
    private val repository: UserRepository,
    private val tokenService: TokenService,
    private val roleService: UserRoleService,
) {
    fun register(user: RegisterDto): Result<UserDto, Unit> {
        val encoded = encoder.encode(user.password)
        val newUser = User(
            username = user.username,
            password = encoded
        )
        return try {
            val savedUser = repository.save(newUser)
            Result.Success(
                UserDto(
                    id = savedUser.id,
                    username = savedUser.username,
                )
            )
        } catch (e: Exception) {
            Result.Failure(Unit)
        }
    }

    fun registerAdmin(user: RegisterDto): Result<UserDto, Unit> {
        val result = register(user)
        if (result is Result.Success) {
            val savedUser = repository.findByUsername(user.username)!!
            roleService.assignRoleToUser(savedUser.id, Role.ADMIN)
        }
        return result
    }

    fun login(user: LoginDto): Result<TokenDto, Unit> {
        val existingUser = repository.findByUsername(user.username)
            ?: return Result.Failure(Unit)
        if (!encoder.matches(user.password, existingUser.password)) {
            return Result.Failure(Unit)
        }
        val roles = roleService.getRolesForUser(existingUser.id)
        val result = tokenService.issueAccessToken(
            subject = existingUser.id.toString(),
            roles = roles.toSet()
        )
        return when (result) {
            is Result.Success -> Result.Success(TokenDto(result.value.tokenValue))
            is Result.Failure -> result
        }
    }
}
