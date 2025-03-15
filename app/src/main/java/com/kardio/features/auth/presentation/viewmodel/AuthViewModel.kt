// features/auth/presentation/viewmodel/AuthViewModel.kt
package com.kardio.features.auth.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import com.kardio.features.auth.domain.usecase.ForgotPasswordUseCase
import com.kardio.features.auth.domain.usecase.LoginUseCase
import com.kardio.features.auth.domain.usecase.RegisterUseCase
import com.kardio.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : BaseViewModel<AuthViewModel.AuthUiState, AuthViewModel.AuthUiEvent>(AuthUiState()) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val errors = mutableMapOf<String, String?>()

            val emailError = ValidationUtils.validateEmail(email)
            if (emailError != null) {
                errors["email"] = emailError
            }

            val passwordError = ValidationUtils.validatePassword(password)
            if (passwordError != null) {
                errors["password"] = passwordError
            }

            if (errors.isNotEmpty()) {
                _authState.value = AuthState.Error(errors)
                return@launch
            }

            try {
                loginUseCase(email, password)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                errors["general"] = e.message ?: "Đăng nhập thất bại"
                _authState.value = AuthState.Error(errors)
            }
        }
    }

    fun register(displayName: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val errors = mutableMapOf<String, String?>()

            val displayNameError = ValidationUtils.validateDisplayName(displayName)
            if (displayNameError != null) {
                errors["displayName"] = displayNameError
            }

            val emailError = ValidationUtils.validateEmail(email)
            if (emailError != null) {
                errors["email"] = emailError
            }

            val passwordError = ValidationUtils.validatePassword(password)
            if (passwordError != null) {
                errors["password"] = passwordError
            }

            val confirmPasswordError = ValidationUtils.validateConfirmPassword(password, confirmPassword)
            if (confirmPasswordError != null) {
                errors["confirmPassword"] = confirmPasswordError
            }

            if (errors.isNotEmpty()) {
                _authState.value = AuthState.Error(errors)
                return@launch
            }

            try {
                registerUseCase(displayName, email, password)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                errors["general"] = e.message ?: "Đăng ký thất bại"
                _authState.value = AuthState.Error(errors)
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val errors = mutableMapOf<String, String?>()

            val emailError = ValidationUtils.validateEmail(email)
            if (emailError != null) {
                errors["email"] = emailError
                _authState.value = AuthState.Error(errors)
                return@launch
            }

            try {
                forgotPasswordUseCase(email)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                errors["general"] = e.message ?: "Gửi email đặt lại mật khẩu thất bại"
                _authState.value = AuthState.Error(errors)
            }
        }
    }

    fun reset() {
        _authState.value = AuthState.Initial
    }

    // UI State
    data class AuthUiState(
        val isLoading: Boolean = false
    ) : UiState

    // UI Events
    sealed class AuthUiEvent : UiEvent

    // Auth States
    sealed class AuthState {
        object Initial : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val errors: Map<String, String?>) : AuthState()
    }
}