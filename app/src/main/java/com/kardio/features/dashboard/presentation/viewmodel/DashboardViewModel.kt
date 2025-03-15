// features/dashboard/presentation/viewmodel/DashboardViewModel.kt
package com.kardio.features.dashboard.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import com.kardio.features.dashboard.domain.model.Class
import com.kardio.features.dashboard.domain.model.Folder
import com.kardio.features.dashboard.domain.model.StreakData
import com.kardio.features.dashboard.domain.model.StudyModule
import com.kardio.features.dashboard.domain.usecase.GetDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase
) : BaseViewModel<DashboardViewModel.DashboardUiState, DashboardViewModel.DashboardUiEvent>(
    DashboardUiState()
) {

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                updateState { it.copy(isLoading = true, error = null) }

                val dashboardData = getDashboardDataUseCase()

                updateState {
                    it.copy(
                        isLoading = false,
                        folders = dashboardData.folders,
                        studyModules = dashboardData.studyModules,
                        classes = dashboardData.classes,
                        streakData = dashboardData.streakData
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load dashboard data"
                    )
                }
            }
        }
    }

    fun refreshDashboard() {
        loadDashboardData()
    }

    fun navigateToFolder(folderId: String) {
        emitEvent(DashboardUiEvent.NavigateToFolder(folderId))
    }

    fun navigateToStudyModule(moduleId: String) {
        emitEvent(DashboardUiEvent.NavigateToStudyModule(moduleId))
    }

    fun navigateToClass(classId: String) {
        emitEvent(DashboardUiEvent.NavigateToClass(classId))
    }

    // UI State for Dashboard screen
    data class DashboardUiState(
        val isLoading: Boolean = false,
        val folders: List<Folder> = emptyList(),
        val studyModules: List<StudyModule> = emptyList(),
        val classes: List<Class> = emptyList(),
        val streakData: StreakData? = null,
        val error: String? = null
    ) : UiState

    // UI Events from Dashboard screen
    sealed class DashboardUiEvent : UiEvent {
        data class NavigateToFolder(val folderId: String) : DashboardUiEvent()
        data class NavigateToStudyModule(val moduleId: String) : DashboardUiEvent()
        data class NavigateToClass(val classId: String) : DashboardUiEvent()
    }
}