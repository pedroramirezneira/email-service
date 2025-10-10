package com.email.service.feature.email.provider

import com.email.service.config.email.EmailProperties
import org.springframework.stereotype.Service

@Service
class EmailProviderRegistry(
    private val strategies: List<EmailProviderStrategy>,
    private val properties: EmailProperties,
) {
    fun getStrategyFor(provider: EmailProvider): EmailProviderStrategy? {
        return strategies.find { it.provider == provider }
    }

    fun getDefaultStrategy(): EmailProviderStrategy? {
        return getStrategyFor(properties.defaultProvider) ?: strategies.firstOrNull()
    }

    fun getNextStrategy(strategy: EmailProviderStrategy): EmailProviderStrategy? {
        val index = strategies.indexOf(strategy)
        return when {
            index == -1 -> strategies.firstOrNull()
            else -> strategies[(index + 1) % strategies.size]
        }
    }
}
