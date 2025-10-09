package com.email.service.feature.email

import com.email.service.common.Result
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/emails")
class EmailController(
    val emailService: EmailService,
) {

    @PostMapping
    fun sendEmail(@RequestBody email: EmailDto): ResponseEntity<Map<String, Any>> {
        val result = emailService.sendEmail(email)
        return when (result) {
            is Result.Success -> ResponseEntity.ok(mapOf("message" to "Email sent successfully"))
            is Result.Failure -> when (val error = result.error) {
                is EmailFailure.InvalidEmailAddress -> ResponseEntity.badRequest().body(
                    mapOf("error" to "Invalid email address: ${error.address}")
                )

                is EmailFailure.LimitExceeded -> ResponseEntity.status(429).body(
                    mapOf("error" to "Email limit exceeded: ${error.limit}")
                )

                is EmailFailure.NoProviderAvailable -> ResponseEntity.status(503).body(
                    mapOf("error" to "No email provider available")
                )

                is EmailFailure.ProviderError -> ResponseEntity.status(503).body(
                    mapOf("error" to "Email provider error: ${error.message}")
                )

                is EmailFailure.UnknownError -> ResponseEntity.status(500).body(
                    mapOf("error" to "Unknown error occurred")
                )
            }
        }
    }
}
