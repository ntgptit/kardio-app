package com.kardio.features.module.data.remote.datasource

import com.kardio.core.base.ResultWrapper
import com.kardio.core.di.IoDispatcher
import com.kardio.features.module.data.remote.api.ModuleApiService
import com.kardio.features.module.data.remote.dto.CreatorDto
import com.kardio.features.module.data.remote.dto.FlashcardDto
import com.kardio.features.module.data.remote.dto.ModuleDto
import com.kardio.features.module.domain.model.Creator
import com.kardio.features.module.domain.model.Flashcard
import com.kardio.features.module.domain.model.StudyModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ModuleRemoteDataSource @Inject constructor(
    private val moduleApiService: ModuleApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getModuleById(moduleId: String): ResultWrapper<StudyModule> = withContext(dispatcher) {
        try {
            val response = moduleApiService.getModuleById(moduleId)
            if (response.isSuccessful) {
                val moduleDto = response.body()
                if (moduleDto != null) {
                    ResultWrapper.Success(moduleDto.toDomainModel())
                } else {
                    ResultWrapper.Error(IllegalStateException("Empty response body"))
                }
            } else {
                ResultWrapper.Error(Exception("Failed to fetch module: ${response.code()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching module from API")
            ResultWrapper.Error(e)
        }
    }

    suspend fun getFlashcardsByModuleId(moduleId: String): ResultWrapper<List<Flashcard>> = withContext(dispatcher) {
        try {
            val response = moduleApiService.getFlashcardsByModuleId(moduleId)
            if (response.isSuccessful) {
                val flashcardDtos = response.body()
                if (flashcardDtos != null) {
                    ResultWrapper.Success(flashcardDtos.map { it.toDomainModel() })
                } else {
                    ResultWrapper.Error(IllegalStateException("Empty response body"))
                }
            } else {
                ResultWrapper.Error(Exception("Failed to fetch flashcards: ${response.code()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching flashcards from API")
            ResultWrapper.Error(e)
        }
    }

    suspend fun getCreatorInfo(creatorId: String): ResultWrapper<Creator> = withContext(dispatcher) {
        try {
            val response = moduleApiService.getCreatorById(creatorId)
            if (response.isSuccessful) {
                val creatorDto = response.body()
                if (creatorDto != null) {
                    ResultWrapper.Success(creatorDto.toDomainModel())
                } else {
                    ResultWrapper.Error(IllegalStateException("Empty response body"))
                }
            } else {
                ResultWrapper.Error(Exception("Failed to fetch creator: ${response.code()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching creator from API")
            ResultWrapper.Error(e)
        }
    }

    suspend fun updateModuleLastOpened(moduleId: String): ResultWrapper<Unit> = withContext(dispatcher) {
        try {
            val timestamp = System.currentTimeMillis()
            val response = moduleApiService.updateLastOpenedTimestamp(moduleId, timestamp)
            if (response.isSuccessful) {
                ResultWrapper.Success(Unit)
            } else {
                ResultWrapper.Error(Exception("Failed to update last opened: ${response.code()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating last opened timestamp")
            ResultWrapper.Error(e)
        }
    }

    suspend fun updateFlashcardStar(flashcardId: String, isStarred: Boolean): ResultWrapper<Boolean> = withContext(dispatcher) {
        try {
            val response = moduleApiService.updateFlashcardStarred(flashcardId, isStarred)
            if (response.isSuccessful) {
                ResultWrapper.Success(isStarred)
            } else {
                ResultWrapper.Error(Exception("Failed to update star status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating flashcard star status")
            ResultWrapper.Error(e)
        }
    }

    suspend fun updateFlashcardMasteryLevel(flashcardId: String, level: Int): ResultWrapper<Int> = withContext(dispatcher) {
        try {
            val response = moduleApiService.updateFlashcardMasteryLevel(flashcardId, level)
            if (response.isSuccessful) {
                ResultWrapper.Success(level)
            } else {
                ResultWrapper.Error(Exception("Failed to update mastery level: ${response.code()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error updating flashcard mastery level")
            ResultWrapper.Error(e)
        }
    }
}