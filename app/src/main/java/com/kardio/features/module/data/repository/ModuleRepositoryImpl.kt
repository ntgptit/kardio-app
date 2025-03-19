// features/module/data/repository/ModuleRepositoryImpl.kt
package com.kardio.features.module.data.repository

import com.kardio.core.base.ResultWrapper
import com.kardio.core.di.IoDispatcher
import com.kardio.features.module.data.local.datasource.ModuleLocalDataSource
import com.kardio.features.module.data.remote.datasource.ModuleRemoteDataSource
import com.kardio.features.module.domain.model.Creator
import com.kardio.features.module.domain.model.Flashcard
import com.kardio.features.module.domain.model.StudyModule
import com.kardio.features.module.domain.repository.ModuleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class ModuleRepositoryImpl @Inject constructor(
    private val remoteDataSource: ModuleRemoteDataSource,
    private val localDataSource: ModuleLocalDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ModuleRepository {

    override suspend fun getModuleById(moduleId: String): Flow<ResultWrapper<StudyModule>> = flow {
        emit(ResultWrapper.Loading)
        delay(500) // Simulate network delay
        emit(ResultWrapper.Success(MockData.mockModule))
    }.flowOn(dispatcher)

    override suspend fun getFlashcardsByModuleId(moduleId: String): Flow<ResultWrapper<List<Flashcard>>> = flow {
        emit(ResultWrapper.Loading)
        delay(500) // Simulate network delay
        emit(ResultWrapper.Success(MockData.mockFlashcards))
    }.flowOn(dispatcher)

    override suspend fun getCreatorInfo(creatorId: String): Flow<ResultWrapper<Creator>> = flow {
        emit(ResultWrapper.Loading)
        delay(500) // Simulate network delay
        emit(ResultWrapper.Success(MockData.mockCreator))
    }.flowOn(dispatcher)

    override suspend fun updateModuleLastOpened(moduleId: String): ResultWrapper<Unit> {
        // Simulate success without actual database update
        return ResultWrapper.Success(Unit)
    }

    override suspend fun toggleFlashcardStar(flashcardId: String, isStarred: Boolean): ResultWrapper<Boolean> {
        // Simulate toggling star state
        return ResultWrapper.Success(isStarred)
    }

    override suspend fun updateFlashcardMasteryLevel(flashcardId: String, level: Int): ResultWrapper<Int> {
        // Simulate updating mastery level
        return ResultWrapper.Success(level)
    }
}