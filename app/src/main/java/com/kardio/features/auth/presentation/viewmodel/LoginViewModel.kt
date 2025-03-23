package com.kardio.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kardio.features.auth.domain.usecase.LoginUseCase
import com.kardio.features.auth.domain.usecase.ValidateEmailUseCase
import com.kardio.features.auth.domain.usecase.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel cho màn hình đăng nhập
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    // UI State cho màn hình login
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Thực hiện đăng nhập
     */
    fun login(email: String, password: String) {
        // Validate input
        val emailResult = validateEmailUseCase(email)
        val passwordResult = validatePasswordUseCase(password)

        val hasError = listOf(emailResult, passwordResult).any { !it.successful }

        // Update UI state với các lỗi nếu có
        _uiState.update {
            it.copy(
                emailError = if (emailResult.successful) null else emailResult.errorMessage,
                passwordError = if (passwordResult.successful) null else passwordResult.errorMessage
            )
        }

        // Nếu có lỗi, không tiếp tục đăng nhập
        if (hasError) {
            return
        }

        // Tiến hành đăng nhập nếu validation thành công
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val result = loginUseCase(email, password)

                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exceptionOrNull()?.message ?: "Đăng nhập thất bại"
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Login error")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Đã xảy ra lỗi khi đăng nhập"
                    )
                }
            }
        }
    }

    /**
     * Xóa thông báo lỗi
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

/**
 * UI State cho màn hình login
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null
)