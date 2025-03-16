// File: app/src/main/java/com/kardio/features/library/presentation/viewmodel/ClassesViewModel.kt
package com.kardio.features.library.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import com.kardio.core.di.IoDispatcher
import com.kardio.features.library.domain.model.ClassModel
import com.kardio.features.library.domain.repository.LibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<ClassesViewModel.ClassesUiState, ClassesViewModel.ClassesUiEvent>(
    ClassesUiState()
) {
    // UI State cho màn hình Classes
    data class ClassesUiState(
        val classes: List<ClassModel> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val isCreating: Boolean = false,
        val validationError: String? = null
    ) : UiState

    // UI Events từ màn hình Classes
    sealed class ClassesUiEvent : UiEvent {
        data class NavigateToClassDetail(val id: String, val name: String) : ClassesUiEvent()
        object ClassCreated : ClassesUiEvent()
    }

    init {
        loadClasses()
    }

    /**
     * Tải danh sách lớp học
     */
    private fun loadClasses(forceRefresh: Boolean = false) {
        updateState { it.copy(isLoading = true) }

        launchWithIO {
            try {
                val classes = libraryRepository.getClasses(forceRefresh)
                updateState {
                    it.copy(
                        classes = classes,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Không thể tải danh sách lớp học"
                    )
                }
            }
        }
    }

    /**
     * Làm mới danh sách lớp học
     */
    fun refreshClasses() {
        loadClasses(true)
    }

    /**
     * Tạo lớp học mới
     */
    fun createClass(name: String, description: String, allowMembersToAdd: Boolean) {
        if (name.trim().isEmpty()) {
            updateState { it.copy(validationError = "Tên lớp học không được để trống") }
            return
        }

        updateState { it.copy(isCreating = true, validationError = null) }

        launchWithIO {
            try {
                val newClass = libraryRepository.createClass(name, description, allowMembersToAdd)
                val updatedClasses = uiState.value.classes.toMutableList().apply {
                    add(newClass)
                }
                updateState {
                    it.copy(
                        classes = updatedClasses,
                        isCreating = false
                    )
                }
                emitEvent(ClassesUiEvent.ClassCreated)
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
     * Điều hướng đến chi tiết lớp học
     */
    fun navigateToClassDetail(classModel: ClassModel) {
        emitEvent(ClassesUiEvent.NavigateToClassDetail(classModel.id, classModel.name))
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