// File: app/src/main/java/com/kardio/features/library/presentation/viewmodel/StudySetsViewModel.kt
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
class StudySetsViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<StudySetsViewModel.StudySetsUiState, StudySetsViewModel.StudySetsUiEvent>(
    StudySetsUiState()
) {
    // UI State cho màn hình StudySets
    data class StudySetsUiState(
        val studySets: List<StudySet> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val filter: String? = null,
        val searchQuery: String = ""
    ) : UiState

    // UI Events từ màn hình StudySets
    sealed class StudySetsUiEvent : UiEvent {
        data class NavigateToStudySetDetail(val id: String, val title: String) : StudySetsUiEvent()
    }

    init {
        loadStudySets()
    }

    /**
     * Tải danh sách study sets
     */
    private fun loadStudySets(filter: String? = null, forceRefresh: Boolean = false) {
        updateState { it.copy(isLoading = true) }

        launchWithIO {
            try {
                val studySets = libraryRepository.getStudySets(filter, forceRefresh)
                updateState {
                    it.copy(
                        studySets = studySets,
                        isLoading = false,
                        error = null,
                        filter = filter
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
     * Áp dụng bộ lọc
     */
    fun applyFilter(filter: String) {
        if (filter == uiState.value.filter) return
        loadStudySets(filter)
    }

    /**
     * Làm mới danh sách study sets
     */
    fun refreshStudySets() {
        loadStudySets(uiState.value.filter, true)
    }

    /**
     * Điều hướng đến chi tiết study set
     */
    fun navigateToStudySetDetail(studySet: StudySet) {
        emitEvent(StudySetsUiEvent.NavigateToStudySetDetail(studySet.id, studySet.title))
    }

    /**
     * Cập nhật từ khóa tìm kiếm
     */
    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }
        // Implement search logic here if needed
    }

    /**
     * Đánh dấu đã hiển thị lỗi
     */
    fun errorShown() {
        updateState { it.copy(error = null) }
    }
}