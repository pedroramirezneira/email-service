package com.email.service.feature.email

import com.email.service.common.Result
import com.email.service.feature.email.provider.EmailProvider
import com.email.service.feature.email.provider.EmailProviderStrategy

class MockEmailStrategy : EmailProviderStrategy {
    override val provider = EmailProvider.SPARKPOST

    override fun send(email: EmailDto): Result<Unit, EmailFailure> {
        return Result.Success(Unit)
    }
}
