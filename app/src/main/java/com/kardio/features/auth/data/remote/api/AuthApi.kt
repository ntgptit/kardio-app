// features/auth/data/remote/AuthApi.kt
package com.kardio.features.auth.data.remote

import com.kardio.features.auth.data.repository.AuthRepositoryImpl.ForgotPasswordRequest
import com.kardio.features.auth.data.repository.AuthRepositoryImpl.LoginRequest
import com.kardio.features.auth.data.repository.AuthRepositoryImpl.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<Unit>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<Unit>
}