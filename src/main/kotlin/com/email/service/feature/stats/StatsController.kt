package com.email.service.feature.stats

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stats")
class StatsController(
    private val service: StatsService,
) {
    @GetMapping
    fun getStats(@AuthenticationPrincipal jwt: Jwt): ResponseEntity<Map<String, Any>> {
        val result = service.getStats()
        return ResponseEntity.ok(
            mapOf("message" to "Stats retrieved successfully", "data" to result)
        )
    }
}
