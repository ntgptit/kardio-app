// File: app/src/main/java/com/kardio/features/library/presentation/viewmodel/LibraryViewModel.kt
package com.kardio.features.library.presentation.viewmodel

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
class LibraryViewModel @Inject constructor() : BaseViewModel<LibraryViewModel.LibraryUiState, LibraryViewModel.LibraryUiEvent>(
    LibraryUiState()
) {
    // UI State cho màn hình Library
    data class LibraryUiState(
        val selectedTabIndex: Int = 0,
        val isLoading: Boolean = false,
        val searchQuery: String = ""
    ) : UiState

    // UI Events từ màn hình Library
    sealed class LibraryUiEvent : UiEvent {
        object NavigateToCreateStudyModule : LibraryUiEvent()
        object NavigateToCreateFolder : LibraryUiEvent()
        object NavigateToCreateClass : LibraryUiEvent()
        data class NavigateToStudyModuleDetail(val moduleId: String, val moduleName: String) : LibraryUiEvent()
        data class NavigateToFolderDetail(val folderId: String, val folderName: String) : LibraryUiEvent()
        data class NavigateToClassDetail(val classId: String, val className: String) : LibraryUiEvent()
    }

    /**
     * Cập nhật tab được chọn
     */
    fun updateSelectedTab(index: Int) {
        if (uiState.value.selectedTabIndex != index) {
            updateState { it.copy(selectedTabIndex = index) }
        }
    }

    /**
     * Xử lý sự kiện tìm kiếm
     */
    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    /**
     * Xử lý sự kiện điều hướng đến chi tiết study module
     */
    fun navigateToStudyModuleDetail(moduleId: String, moduleName: String) {
        emitEvent(LibraryUiEvent.NavigateToStudyModuleDetail(moduleId, moduleName))
    }

    /**
     * Xử lý sự kiện điều hướng đến chi tiết folder
     */
    fun navigateToFolderDetail(folderId: String, folderName: String) {
        emitEvent(LibraryUiEvent.NavigateToFolderDetail(folderId, folderName))
    }

    /**
     * Xử lý sự kiện điều hướng đến chi tiết class
     */
    fun navigateToClassDetail(classId: String, className: String) {
        emitEvent(LibraryUiEvent.NavigateToClassDetail(classId, className))
    }

    /**
     * Xử lý sự kiện tạo study module mới
     */
    fun navigateToCreateStudyModule() {
        emitEvent(LibraryUiEvent.NavigateToCreateStudyModule)
    }

    /**
     * Xử lý sự kiện tạo folder mới
     */
    fun navigateToCreateFolder() {
        emitEvent(LibraryUiEvent.NavigateToCreateFolder)
    }

    /**
     * Xử lý sự kiện tạo class mới
     */
    fun navigateToCreateClass() {
        emitEvent(LibraryUiEvent.NavigateToCreateClass)
    }
}