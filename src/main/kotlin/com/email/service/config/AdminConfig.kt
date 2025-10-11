package com.email.service.config

import com.email.service.feature.auth.AuthService
import com.email.service.feature.auth.RegisterDto
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdminConfig {
    @Bean
    fun addAdmins(authService: AuthService) = CommandLineRunner {
        authService.registerAdmin(
            RegisterDto(
                username = "pedroramirezneira",
                password = "pedro123",
            )
        )
    }
}
