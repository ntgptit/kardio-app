package com.kardio.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kardio.core.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho màn hình Home chứa Bottom Navigation
 */
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /**
     * Thay đổi tab được chọn
     */
    fun changeTab(index: Int) {
        if (_uiState.value.selectedTabIndex != index) {
            _uiState.update { it.copy(selectedTabIndex = index) }
        }
    }

    /**
     * Xử lý sự kiện thiết lập
     */
    fun handleSettings() {
        viewModelScope.launch {
            // TODO: Implement settings navigation
        }
    }

    /**
     * Xử lý sự kiện đăng xuất
     */
    fun handleLogout() {
        viewModelScope.launch {
            // TODO: Implement logout logic
        }
    }
}

/**
 * UI State cho màn hình home
 */
data class HomeUiState(
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false
) : UiState