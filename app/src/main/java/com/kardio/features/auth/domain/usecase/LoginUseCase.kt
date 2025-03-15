// features/auth/domain/usecase/LoginUseCase.kt
package com.kardio.features.auth.domain.usecase

import com.kardio.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        return repository.login(email, password)
    }
}