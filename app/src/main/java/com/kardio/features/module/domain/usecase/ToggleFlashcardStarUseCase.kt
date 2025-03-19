// features/module/domain/usecase/ToggleFlashcardStarUseCase.kt
package com.kardio.features.module.domain.usecase

import com.kardio.core.base.ResultWrapper
import com.kardio.core.di.IoDispatcher
import com.kardio.features.module.domain.repository.ModuleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleFlashcardStarUseCase @Inject constructor(
    private val moduleRepository: ModuleRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(flashcardId: String, isStarred: Boolean): ResultWrapper<Boolean> =
        withContext(dispatcher) {
            moduleRepository.toggleFlashcardStar(flashcardId, isStarred)
        }
}