// features/auth/domain/usecase/ForgotPasswordUseCase.kt
package com.kardio.features.auth.domain.usecase

import com.kardio.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String) {
        return repository.forgotPassword(email)
    }
}