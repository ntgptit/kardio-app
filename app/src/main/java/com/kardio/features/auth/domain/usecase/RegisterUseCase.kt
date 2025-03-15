// features/auth/domain/usecase/RegisterUseCase.kt
package com.kardio.features.auth.domain.usecase

import com.kardio.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(displayName: String, email: String, password: String) {
        return repository.register(displayName, email, password)
    }
}