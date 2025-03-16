// File: app/src/main/java/com/kardio/features/library/presentation/viewmodel/CreateFolderViewModel.kt
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
class CreateFolderViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<CreateFolderViewModel.CreateFolderUiState, CreateFolderViewModel.CreateFolderUiEvent>(
    CreateFolderUiState()
) {
    // UI State cho màn hình Create Folder
    data class CreateFolderUiState(
        val isCreating: Boolean = false,
        val error: String? = null,
        val validationError: String? = null
    ) : UiState

    // UI Events từ màn hình Create Folder
    sealed class CreateFolderUiEvent : UiEvent {
        object FolderCreated : CreateFolderUiEvent()
    }

    /**
     * Tạo folder mới
     */
    fun createFolder(name: String, description: String?) {
        // Validate input
        if (name.trim().isEmpty()) {
            updateState { it.copy(validationError = "Tên thư mục không được để trống") }
            return
        }

        updateState { it.copy(isCreating = true, validationError = null) }

        launchWithIO {
            try {
                val folder = libraryRepository.createFolder(name, description)

                // Update state
                updateState { it.copy(isCreating = false, error = null) }

                // Emit event
                emitEvent(CreateFolderUiEvent.FolderCreated)
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isCreating = false,
                        error = e.message ?: "Không thể tạo thư mục"
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