package com.kardio.features.module.data.local.dao

import com.kardio.features.module.data.local.entity.FlashcardEntity

// src/main/java/com/kardio/features/module/data/local/dao/MockFlashcardDao.kt
class MockFlashcardDao : FlashcardDao {
    override suspend fun getFlashcardsByModuleId(moduleId: String): List<FlashcardEntity> {
        return MockData.mockFlashcards.map { FlashcardEntity.fromDomainModel(it, moduleId) }
    }

    override suspend fun getFlashcardById(flashcardId: String): FlashcardEntity? {
        return MockData.mockFlashcards.find { it.id == flashcardId }?.let {
            FlashcardEntity.fromDomainModel(it, "module1")
        }
    }

    override suspend fun insertFlashcards(flashcards: List<FlashcardEntity>) {
        TODO("Not yet implemented")
    }


    override suspend fun updateFlashcardStarred(flashcardId: String, isStarred: Boolean) {
        // Do nothing for mock
    }

    override suspend fun updateFlashcardMasteryLevel(flashcardId: String, level: Int) {
        // Do nothing for mock
    }

    override suspend fun updateFlashcardLastReviewed(flashcardId: String, timestamp: Long) {
        // Do nothing for mock
    }
}