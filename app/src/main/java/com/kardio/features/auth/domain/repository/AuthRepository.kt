// features/auth/domain/repository/AuthRepository.kt
package com.kardio.features.auth.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun register(displayName: String, email: String, password: String)
    suspend fun forgotPassword(email: String)
}