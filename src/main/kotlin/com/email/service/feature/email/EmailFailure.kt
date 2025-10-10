package com.email.service.feature.email

sealed interface EmailFailure {
    data class InvalidEmailAddress(val address: String) : EmailFailure
    data class LimitExceeded(val limit: Int) : EmailFailure
    data class ProviderError(val message: String) : EmailFailure
    data object NoProviderAvailable : EmailFailure
    data object MaxRetriesExceeded : EmailFailure
    data object UnknownError : EmailFailure
}
