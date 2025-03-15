// features/auth/data/repository/AuthRepositoryImpl.kt
package com.kardio.features.auth.data.repository

import com.kardio.features.auth.data.remote.api.AuthApi
import com.kardio.features.auth.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun login(email: String, password: String) {
        try {
            val response = authApi.login(LoginRequest(email, password))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw Exception("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.")
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> throw Exception("Email hoặc mật khẩu không đúng.")
                else -> throw Exception("Đã có lỗi xảy ra. Vui lòng thử lại sau.")
            }
        }
    }

    override suspend fun register(displayName: String, email: String, password: String) {
        try {
            val response = authApi.register(RegisterRequest(displayName, email, password))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw Exception("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.")
        } catch (e: HttpException) {
            when (e.code()) {
                409 -> throw Exception("Email đã được sử dụng.")
                else -> throw Exception("Đã có lỗi xảy ra. Vui lòng thử lại sau.")
            }
        }
    }

    override suspend fun forgotPassword(email: String) {
        try {
            val response = authApi.forgotPassword(ForgotPasswordRequest(email))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            throw Exception("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.")
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> throw Exception("Email không tồn tại trong hệ thống.")
                else -> throw Exception("Đã có lỗi xảy ra. Vui lòng thử lại sau.")
            }
        }
    }

    data class LoginRequest(val email: String, val password: String)
    data class RegisterRequest(val displayName: String, val email: String, val password: String)
    data class ForgotPasswordRequest(val email: String)
}