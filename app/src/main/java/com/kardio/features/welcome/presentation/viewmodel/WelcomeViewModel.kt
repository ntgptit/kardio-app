// features/welcome/presentation/viewmodel/WelcomeViewModel.kt
package com.kardio.features.welcome.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<WelcomeViewModel.WelcomeUiState, WelcomeViewModel.WelcomeEvent>(
    WelcomeUiState()
) {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    fun onGoogleSignInClick() {
        viewModelScope.launch {
            // Ở đây sẽ có xử lý logic đăng nhập Google
            // Tạm thời chỉ điều hướng đến màn Home
            _navigationEvent.emit(NavigationEvent.NavigateToHome)
        }
    }

    fun onEmailSignUpClick() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToRegister)
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToLogin)
        }
    }

    // UI State cho màn hình welcome
    data class WelcomeUiState(
        val isLoading: Boolean = false
    ) : UiState

    // UI Events từ màn hình welcome
    sealed class WelcomeEvent : UiEvent

    // Navigation events
    sealed class NavigationEvent {
        object NavigateToHome : NavigationEvent()
        object NavigateToRegister : NavigationEvent()
        object NavigateToLogin : NavigationEvent()
    }
}