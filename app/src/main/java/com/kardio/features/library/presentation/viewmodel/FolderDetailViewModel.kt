// File: app/src/main/java/com/kardio/features/library/presentation/viewmodel/FolderDetailViewModel.kt
package com.kardio.features.library.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import com.kardio.core.di.IoDispatcher
import com.kardio.features.library.domain.model.StudySet
import com.kardio.features.library.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderDetailViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<FolderDetailViewModel.FolderDetailUiState, FolderDetailViewModel.FolderDetailUiEvent>(
    FolderDetailUiState()
) {
    // UI State cho màn hình Folder Detail
    data class FolderDetailUiState(
        val folderId: String = "",
        val studySets: List<StudySet> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    ) : UiState

    // UI Events từ màn hình Folder Detail
    sealed class FolderDetailUiEvent : UiEvent {
        data class NavigateToStudySetDetail(val id: String, val title: String) : FolderDetailUiEvent()
    }

    /**
     * Tải danh sách study sets của một folder
     */
    fun loadStudySetsByFolder(folderId: String, forceRefresh: Boolean = false) {
        updateState { it.copy(isLoading = true, folderId = folderId) }

        launchWithIO {
            try {
                val studySets = libraryRepository.getStudySetsByFolder(folderId, forceRefresh)
                updateState {
                    it.copy(
                        studySets = studySets,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Không thể tải danh sách học phần"
                    )
                }
            }
        }
    }

    /**
     * Làm mới danh sách study sets
     */
    fun refreshStudySets() {
        val folderId = uiState.value.folderId
        if (folderId.isNotEmpty()) {
            loadStudySetsByFolder(folderId, true)
        }
    }

    /**
     * Điều hướng đến chi tiết study set
     */
    fun navigateToStudySetDetail(studySet: StudySet) {
        emitEvent(FolderDetailUiEvent.NavigateToStudySetDetail(studySet.id, studySet.title))
    }

    /**
     * Đánh dấu đã hiển thị lỗi
     */
    fun errorShown() {
        updateState { it.copy(error = null) }
    }
}