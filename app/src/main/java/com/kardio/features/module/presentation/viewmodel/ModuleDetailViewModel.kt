// features/module/presentation/viewmodel/ModuleDetailViewModel.kt
package com.kardio.features.module.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.kardio.core.base.BaseViewModel
import com.kardio.core.base.ResultWrapper
import com.kardio.core.base.UiEvent
import com.kardio.core.base.UiState
import com.kardio.features.module.domain.model.Creator
import com.kardio.features.module.domain.model.Flashcard
import com.kardio.features.module.domain.model.StudyModule
import com.kardio.features.module.domain.usecase.GetModuleDetailsUseCase
import com.kardio.features.module.domain.usecase.GetModuleFlashcardsUseCase
import com.kardio.features.module.domain.usecase.ToggleFlashcardStarUseCase
import com.kardio.features.module.domain.usecase.UpdateLastOpenedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ModuleDetailViewModel @Inject constructor(
    private val getModuleDetailsUseCase: GetModuleDetailsUseCase,
    private val getModuleFlashcardsUseCase: GetModuleFlashcardsUseCase,
    private val updateLastOpenedUseCase: UpdateLastOpenedUseCase,
    private val toggleFlashcardStarUseCase: ToggleFlashcardStarUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ModuleDetailViewModel.ModuleDetailUiState, ModuleDetailViewModel.ModuleDetailEvent>(
    ModuleDetailUiState()
) {
    private val moduleId: String = checkNotNull(savedStateHandle["moduleId"])

    init {
        loadModuleDetails()
        loadFlashcards()
        updateLastOpened()
    }

    private fun loadModuleDetails() {
        launchWithIO {
            getModuleDetailsUseCase(moduleId).collectLatest { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        updateState { it.copy(
                            isLoading = false,
                            module = result.data,
                            error = null
                        )}
                    }
                    is ResultWrapper.Error -> {
                        updateState { it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )}
                    }
                    is ResultWrapper.Loading -> {
                        updateState { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun loadFlashcards() {
        launchWithIO {
            getModuleFlashcardsUseCase(moduleId).collectLatest { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        updateState { it.copy(
                            isLoading = false,
                            flashcards = result.data,
                            error = null
                        )}
                    }
                    is ResultWrapper.Error -> {
                        updateState { it.copy(
                            isLoading = false,
                            error = result.exception.message
                        )}
                    }
                    is ResultWrapper.Loading -> {
                        updateState { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun updateLastOpened() {
        launchWithIO {
            updateLastOpenedUseCase(moduleId)
        }
    }

    fun onFlashcardStarToggled(flashcardId: String, isStarred: Boolean) {
        launchWithIO {
            toggleFlashcardStarUseCase(flashcardId, isStarred).onSuccess { newStarredState ->
                // Update the flashcard in the list
                val updatedFlashcards = uiState.value.flashcards.map { flashcard ->
                    if (flashcard.id == flashcardId) {
                        flashcard.copy(isStarred = newStarredState)
                    } else {
                        flashcard
                    }
                }
                updateState { it.copy(flashcards = updatedFlashcards) }
            }.onError { error ->
                Timber.e(error, "Failed to toggle star state")
                emitEvent(ModuleDetailEvent.ShowErrorMessage("Failed to update card status"))
            }
        }
    }

    fun onStartLearningClick() {
        emitEvent(ModuleDetailEvent.NavigateToFlashcardStudy(moduleId))
    }

    fun onStartQuizClick() {
        emitEvent(ModuleDetailEvent.NavigateToQuiz(moduleId))
    }

    fun onStartMatchClick() {
        emitEvent(ModuleDetailEvent.NavigateToMatch(moduleId))
    }

    fun onNavigateBack() {
        emitEvent(ModuleDetailEvent.NavigateBack)
    }

    data class ModuleDetailUiState(
        val isLoading: Boolean = true,
        val module: StudyModule? = null,
        val creator: Creator? = null,
        val flashcards: List<Flashcard> = emptyList(),
        val currentFlashcardIndex: Int = 0,
        val error: String? = null
    ) : UiState

    sealed class ModuleDetailEvent : UiEvent {
        data class NavigateToFlashcardStudy(val moduleId: String) : ModuleDetailEvent()
        data class NavigateToQuiz(val moduleId: String) : ModuleDetailEvent()
        data class NavigateToMatch(val moduleId: String) : ModuleDetailEvent()
        data class ShowErrorMessage(val message: String) : ModuleDetailEvent()
        object NavigateBack : ModuleDetailEvent()
    }
}