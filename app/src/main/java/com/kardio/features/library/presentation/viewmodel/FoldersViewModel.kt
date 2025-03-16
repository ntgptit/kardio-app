// File: app/src/main/java/com/kardio/features/library/presentation/viewmodel/FoldersViewModel.kt
package com.kardio.features.library.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import com.kardio.core.di.IoDispatcher
import com.kardio.features.library.domain.model.Folder
import com.kardio.features.library.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<FoldersViewModel.FoldersUiState, FoldersViewModel.FoldersUiEvent>(
    FoldersUiState()
) {
    // UI State cho màn hình Folders
    data class FoldersUiState(
        val folders: List<Folder> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val isCreating: Boolean = false,
        val validationError: String? = null
    ) : UiState

    // UI Events từ màn hình Folders
    sealed class FoldersUiEvent : UiEvent {
        data class NavigateToFolderDetail(val id: String, val name: String) : FoldersUiEvent()
        object FolderCreated : FoldersUiEvent()
    }

    init {
        loadFolders()
    }

    /**
     * Tải danh sách folders
     */
    private fun loadFolders(forceRefresh: Boolean = false) {
        updateState { it.copy(isLoading = true) }

        launchWithIO {
            try {
                val folders = libraryRepository.getFolders(forceRefresh)
                updateState {
                    it.copy(
                        folders = folders,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Không thể tải danh sách thư mục"
                    )
                }
            }
        }
    }

    /**
     * Làm mới danh sách folders
     */
    fun refreshFolders() {
        loadFolders(true)
    }

    /**
     * Tạo folder mới
     */
    fun createFolder(name: String, description: String?) {
        if (name.trim().isEmpty()) {
            updateState { it.copy(validationError = "Tên thư mục không được để trống") }
            return
        }

        updateState { it.copy(isCreating = true, validationError = null) }

        launchWithIO {
            try {
                val newFolder = libraryRepository.createFolder(name, description)
                val updatedFolders = uiState.value.folders.toMutableList().apply {
                    add(newFolder)
                }
                updateState {
                    it.copy(
                        folders = updatedFolders,
                        isCreating = false
                    )
                }
                emitEvent(FoldersUiEvent.FolderCreated)
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
     * Điều hướng đến chi tiết folder
     */
    fun navigateToFolderDetail(folder: Folder) {
        emitEvent(FoldersUiEvent.NavigateToFolderDetail(folder.id, folder.name))
    }

    /**
     * Đánh dấu đã hiển thị lỗi
     */
    fun errorShown() {
        updateState { it.copy(error = null) }
    }

    /**
     * Xóa lỗi xác thực
     */
    fun clearValidationError() {
        updateState { it.copy(validationError = null) }
    }
}