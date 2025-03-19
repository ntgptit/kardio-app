// features/module/domain/repository/ModuleRepository.kt
package com.kardio.features.module.domain.repository

import com.kardio.core.base.ResultWrapper
import com.kardio.features.module.domain.model.Creator
import com.kardio.features.module.domain.model.Flashcard
import com.kardio.features.module.domain.model.StudyModule
import kotlinx.coroutines.flow.Flow

interface ModuleRepository {
    suspend fun getModuleById(moduleId: String): Flow<ResultWrapper<StudyModule>>
    suspend fun getFlashcardsByModuleId(moduleId: String): Flow<ResultWrapper<List<Flashcard>>>
    suspend fun getCreatorInfo(creatorId: String): Flow<ResultWrapper<Creator>>
    suspend fun updateModuleLastOpened(moduleId: String): ResultWrapper<Unit>
    suspend fun toggleFlashcardStar(flashcardId: String, isStarred: Boolean): ResultWrapper<Boolean>
    suspend fun updateFlashcardMasteryLevel(flashcardId: String, level: Int): ResultWrapper<Int>
}