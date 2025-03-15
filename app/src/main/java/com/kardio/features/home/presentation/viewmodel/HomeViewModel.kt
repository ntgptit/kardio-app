package com.kardio.features.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeViewModel.HomeUiState, HomeViewModel.HomeUiEvent>(
    HomeUiState()
) {
    // UI State cho màn hình home
    data class HomeUiState(
        val selectedTabIndex: Int = 0,
        val isLoading: Boolean = false
    ) : UiState

    // UI Events từ màn hình home
    sealed class HomeUiEvent : UiEvent {
        data class NavigateToSettings(val route: String) : HomeUiEvent()
        object NavigateToLogout : HomeUiEvent()
    }

    /**
     * Thay đổi tab được chọn
     */
    fun changeTab(index: Int) {
        if (uiState.value.selectedTabIndex != index) {
            updateState { it.copy(selectedTabIndex = index) }
        }
    }

    /**
     * Xử lý sự kiện thiết lập
     */
    fun handleSettings() {
        emitEvent(HomeUiEvent.NavigateToSettings("settings"))
    }

    /**
     * Xử lý sự kiện đăng xuất
     */
    fun handleLogout() {
        emitEvent(HomeUiEvent.NavigateToLogout)
    }
}