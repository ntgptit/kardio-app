package com.kardio.features.module.data.local.datasource

import com.kardio.core.di.IoDispatcher
import com.kardio.features.module.data.local.dao.FlashcardDao
import com.kardio.features.module.data.local.dao.ModuleDao
import com.kardio.features.module.data.local.entity.CreatorEntity
import com.kardio.features.module.data.local.entity.FlashcardEntity
import com.kardio.features.module.data.local.entity.ModuleEntity
import com.kardio.features.module.domain.model.Creator
import com.kardio.features.module.domain.model.Flashcard
import com.kardio.features.module.domain.model.StudyModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModuleLocalDataSource @Inject constructor(
    private val moduleDao: ModuleDao,
    private val flashcardDao: FlashcardDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getModuleById(moduleId: String): StudyModule? = withContext(dispatcher) {
        moduleDao.getModuleById(moduleId)?.toDomainModel()
    }

    suspend fun getFlashcardsByModuleId(moduleId: String): List<Flashcard> = withContext(dispatcher) {
        flashcardDao.getFlashcardsByModuleId(moduleId).map { it.toDomainModel() }
    }

    suspend fun getCreatorInfo(creatorId: String): Creator? = withContext(dispatcher) {
        moduleDao.getCreatorById(creatorId)?.toDomainModel()
    }

    suspend fun saveModule(module: StudyModule) = withContext(dispatcher) {
        moduleDao.insertModule(ModuleEntity.fromDomainModel(module))
    }

    suspend fun saveFlashcards(moduleId: String, flashcards: List<Flashcard>) = withContext(dispatcher) {
        flashcardDao.insertFlashcards(flashcards.map { FlashcardEntity.fromDomainModel(it, moduleId) })
    }

    suspend fun saveCreator(creator: Creator) = withContext(dispatcher) {
        moduleDao.insertCreator(CreatorEntity.fromDomainModel(creator))
    }

    suspend fun updateModuleLastOpened(moduleId: String) = withContext(dispatcher) {
        moduleDao.updateLastOpenedTimestamp(moduleId, System.currentTimeMillis())
    }

    suspend fun updateFlashcardStar(flashcardId: String, isStarred: Boolean) = withContext(dispatcher) {
        flashcardDao.updateFlashcardStarred(flashcardId, isStarred)
    }

    suspend fun updateFlashcardMasteryLevel(flashcardId: String, level: Int) = withContext(dispatcher) {
        flashcardDao.updateFlashcardMasteryLevel(flashcardId, level)
    }
}