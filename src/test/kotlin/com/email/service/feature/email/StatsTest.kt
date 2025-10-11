package com.email.service.feature.email

import com.email.service.feature.role.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class StatsTest(
    @Autowired private val mockMvc: MockMvc
) {
    @Test
    fun testStatsForAdmin() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/stats"
            ).with(
                jwt().authorities(SimpleGrantedAuthority("ROLE_${Role.ADMIN.name}"))
            )
        )
            .andExpect(status().isOk)
    }

    @Test
    fun testStatsForUser() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/stats"
            ).with(
                jwt()
            )
        )
            .andExpect(status().isForbidden)
    }
}
