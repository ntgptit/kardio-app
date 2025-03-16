// File: app/src/main/java/com/kardio/features/library/presentation/viewmodel/CreateClassViewModel.kt
package com.kardio.features.library.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import com.kardio.core.di.IoDispatcher
import com.kardio.features.library.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateClassViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<CreateClassViewModel.CreateClassUiState, CreateClassViewModel.CreateClassUiEvent>(
    CreateClassUiState()
) {
    // UI State cho màn hình Create Class
    data class CreateClassUiState(
        val isCreating: Boolean = false,
        val error: String? = null,
        val validationError: String? = null
    ) : UiState

    // UI Events từ màn hình Create Class
    sealed class CreateClassUiEvent : UiEvent {
        object ClassCreated : CreateClassUiEvent()
    }

    /**
     * Tạo lớp học mới
     */
    fun createClass(name: String, description: String, allowMembersToAdd: Boolean) {
        // Validate input
        if (name.trim().isEmpty()) {
            updateState { it.copy(validationError = "Tên lớp học không được để trống") }
            return
        }

        updateState { it.copy(isCreating = true, validationError = null) }

        launchWithIO {
            try {
                val classModel = libraryRepository.createClass(name, description, allowMembersToAdd)

                // Update state
                updateState { it.copy(isCreating = false, error = null) }

                // Emit event
                emitEvent(CreateClassUiEvent.ClassCreated)
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isCreating = false,
                        error = e.message ?: "Không thể tạo lớp học"
                    )
                }
            }
        }
    }

    /**
     * Xóa thông báo lỗi
     */
    fun errorShown() {
        updateState { it.copy(error = null) }
    }

    /**
     * Xóa thông báo lỗi xác thực
     */
    fun clearValidationError() {
        updateState { it.copy(validationError = null) }
    }
}