// File: app/src/main/java/com/kardio/core/base/BaseViewModel.kt
package com.kardio.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class that provides common functionality for all ViewModels.
 * Includes support for UI state management and event handling.
 */
abstract class BaseViewModel<State : UiState, Event : UiEvent>(initialState: State) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    // UI Events (one-time events like navigation, showing dialogs)
    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    /**
     * Update the UI state.
     */
    protected fun updateState(reduce: (State) -> State) {
        val newState = reduce(_uiState.value)
        _uiState.value = newState
    }

    /**
     * Emit a one-time UI event.
     */
    protected fun emitEvent(event: Event) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    /**
     * Helper method to launch a coroutine with error handling.
     */
    protected fun launchWithIO(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onError: (Throwable) -> Unit = { handleError(it) },
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                block()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    /**
     * Default error handling.
     * Child classes can override this to provide custom error handling.
     */
    open fun handleError(throwable: Throwable) {
        // Default implementation - can be overridden by child classes
    }
}