package com.email.service.feature.auth

import com.email.service.common.Result
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val service: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody user: RegisterDto): ResponseEntity<Map<String, Any>> {
        return when (val result = service.register(user)) {
            is Result.Failure -> ResponseEntity.status(409).body(
                mapOf("error" to "Username already exists")
            )

            is Result.Success -> ResponseEntity.ok(
                mapOf("message" to "User registered successfully", "data" to result.value)
            )
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody user: LoginDto): ResponseEntity<Map<String, Any>> {
        return when (val result = service.login(user)) {
            is Result.Failure -> ResponseEntity.status(401).body(
                mapOf("error" to "Invalid username or password")
            )

            is Result.Success -> ResponseEntity.ok(
                mapOf("message" to "User logged in successfully", "data" to result.value)
            )
        }
    }
}
