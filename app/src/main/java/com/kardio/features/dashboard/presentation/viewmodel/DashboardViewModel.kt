package com.kardio.features.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kardio.core.base.UiState
import com.kardio.features.dashboard.domain.model.Class
import com.kardio.features.dashboard.domain.model.Folder
import com.kardio.features.dashboard.domain.model.StreakData
import com.kardio.features.dashboard.domain.model.StudyModule
import com.kardio.features.dashboard.domain.usecase.GetDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel cho Dashboard Screen
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    // Navigation events
    private val _navigationEvents = MutableSharedFlow<DashboardNavigationEvent>()
    val navigationEvents: SharedFlow<DashboardNavigationEvent> = _navigationEvents.asSharedFlow()

    init {
        loadDashboardData()
    }

    /**
     * Load dashboard data
     */
    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val dashboardData = getDashboardDataUseCase()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        folders = dashboardData.folders,
                        studyModules = dashboardData.studyModules,
                        classes = dashboardData.classes,
                        streakData = dashboardData.streakData
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading dashboard data")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load dashboard data"
                    )
                }
            }
        }
    }

    /**
     * Refresh dashboard data
     */
    fun refreshDashboard() {
        loadDashboardData()
    }

    /**
     * Navigate to folder detail
     */
    fun navigateToFolder(folderId: String) {
        viewModelScope.launch {
            _navigationEvents.emit(DashboardNavigationEvent.NavigateToFolder(folderId))
        }
    }

    /**
     * Navigate to study module detail
     */
    fun navigateToStudyModule(moduleId: String) {
        viewModelScope.launch {
            _navigationEvents.emit(DashboardNavigationEvent.NavigateToStudyModule(moduleId))
        }
    }

    /**
     * Navigate to class detail
     */
    fun navigateToClass(classId: String) {
        viewModelScope.launch {
            _navigationEvents.emit(DashboardNavigationEvent.NavigateToClass(classId))
        }
    }
}

/**
 * UI State cho Dashboard screen
 */
data class DashboardUiState(
    val isLoading: Boolean = false,
    val folders: List<Folder> = emptyList(),
    val studyModules: List<StudyModule> = emptyList(),
    val classes: List<Class> = emptyList(),
    val streakData: StreakData? = null,
    val error: String? = null
) : UiState

/**
 * Navigation events từ Dashboard
 */
sealed class DashboardNavigationEvent {
    data class NavigateToFolder(val folderId: String) : DashboardNavigationEvent()
    data class NavigateToStudyModule(val moduleId: String) : DashboardNavigationEvent()
    data class NavigateToClass(val classId: String) : DashboardNavigationEvent()
}