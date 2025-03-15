package com.kardio.features.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeViewModel.HomeUiState, HomeViewModel.HomeUiEvent>(
    HomeUiState()
) {
    // Elimina esta declaración duplicada
    // private val _uiState = MutableStateFlow(HomeUiState())
    // val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun changeTab(index: Int) {
        if (uiState.value.selectedTabIndex != index) {
            updateState { it.copy(selectedTabIndex = index) }
        }
    }

    // UI State
    data class HomeUiState(
        val selectedTabIndex: Int = 0
    ) : UiState

    // UI Events
    sealed class HomeUiEvent : UiEvent
}