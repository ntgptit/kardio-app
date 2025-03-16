// File: app/src/main/java/com/kardio/features/library/presentation/viewmodel/ClassDetailViewModel.kt
package com.kardio.features.library.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import com.kardio.core.di.IoDispatcher
import com.kardio.features.library.domain.model.ClassDetail
import com.kardio.features.library.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassDetailViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<ClassDetailViewModel.ClassDetailUiState, ClassDetailViewModel.ClassDetailUiEvent>(
    ClassDetailUiState()
) {
    // UI State cho màn hình Class Detail
    data class ClassDetailUiState(
        val classId: String = "",
        val classDetail: ClassDetail? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val actionMessage: String? = null
    ) : UiState

    // UI Events từ màn hình Class Detail
    sealed class ClassDetailUiEvent : UiEvent {
        data class NavigateToStudySetDetail(val id: String, val title: String) : ClassDetailUiEvent()
        object ClassJoined : ClassDetailUiEvent()
        object ClassLeft : ClassDetailUiEvent()
    }

    /**
     * Tải thông tin chi tiết của một lớp học
     */
    fun loadClassDetail(classId: String) {
        updateState { it.copy(isLoading = true, classId = classId) }

        launchWithIO {
            try {
                val classDetail = libraryRepository.getClassDetail(classId)
                updateState {
                    it.copy(
                        classDetail = classDetail,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Không thể tải thông tin lớp học"
                    )
                }
            }
        }
    }

    /**
     * Tham gia lớp học
     */
    fun joinClass() {
        val classId = uiState.value.classId
        if (classId.isEmpty()) return

        updateState { it.copy(isLoading = true) }

        launchWithIO {
            try {
                libraryRepository.joinClass(classId)

                // Cập nhật thông tin lớp học sau khi tham gia
                val updatedClassDetail = uiState.value.classDetail?.copy(hasJoined = true)

                updateState {
                    it.copy(
                        isLoading = false,
                        classDetail = updatedClassDetail,
                        actionMessage = "Bạn đã tham gia lớp học thành công"
                    )
                }

                emitEvent(ClassDetailUiEvent.ClassJoined)
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Không thể tham gia lớp học"
                    )
                }
            }
        }
    }

    /**
     * Rời khỏi lớp học
     */
    fun leaveClass() {
        val classId = uiState.value.classId
        if (classId.isEmpty()) return

        updateState { it.copy(isLoading = true) }

        launchWithIO {
            try {
                libraryRepository.leaveClass(classId)

                // Cập nhật thông tin lớp học sau khi rời khỏi
                val updatedClassDetail = uiState.value.classDetail?.copy(hasJoined = false)

                updateState {
                    it.copy(
                        isLoading = false,
                        classDetail = updatedClassDetail,
                        actionMessage = "Bạn đã rời khỏi lớp học"
                    )
                }

                emitEvent(ClassDetailUiEvent.ClassLeft)
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Không thể rời khỏi lớp học"
                    )
                }
            }
        }
    }

    /**
     * Đánh dấu đã hiển thị lỗi
     */
    fun errorShown() {
        updateState { it.copy(error = null) }
    }

    /**
     * Đánh dấu đã hiển thị thông báo hành động
     */
    fun actionMessageShown() {
        updateState { it.copy(actionMessage = null) }
    }
}