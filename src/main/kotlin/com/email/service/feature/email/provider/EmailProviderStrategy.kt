package com.email.service.feature.email.provider

import com.email.service.common.Result
import com.email.service.feature.email.EmailDto
import com.email.service.feature.email.EmailFailure

interface EmailProviderStrategy {
    val provider: EmailProvider
    fun send(email: EmailDto): Result<Unit, EmailFailure>
}
