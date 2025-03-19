package com.kardio.features.module.data.local.dao

import com.kardio.features.module.data.local.entity.CreatorEntity
import com.kardio.features.module.data.local.entity.ModuleEntity
import com.kardio.features.module.domain.model.Flashcard

// src/main/java/com/kardio/features/module/data/local/dao/MockModuleDao.kt
class MockModuleDao : ModuleDao {
    override suspend fun getModuleById(moduleId: String): ModuleEntity? {
        return MockData.mockModuleEntity
    }

    override suspend fun getCreatorById(creatorId: String): CreatorEntity? {
        return MockData.mockCreatorEntity
    }

    override suspend fun insertModule(module: ModuleEntity) {
        // Do nothing for mock
    }

    override suspend fun insertCreator(creator: CreatorEntity) {
        // Do nothing for mock
    }

    override suspend fun updateLastOpenedTimestamp(moduleId: String, timestamp: Long) {
        // Do nothing for mock
    }

    override suspend fun getRecentModules(limit: Int): List<ModuleEntity> {
        return listOf(MockData.mockModuleEntity)
    }
}

// Thêm mock data vào MockData.kt
object MockData {
    val mockModuleEntity = ModuleEntity(
        id = "module1",
        title = "Introduction to Biology",
        description = "Learn the basics of biology",
        creatorId = "creator1",
        creatorName = "John Doe",
        lastUpdated = System.currentTimeMillis() - 86400000,
        totalCards = 5,
        isPublic = true,
        lastOpenedTimestamp = null
    )

    val mockCreatorEntity = CreatorEntity(
        id = "creator1",
        name = "John Doe",
        avatar = "https://example.com/avatar.jpg",
        isPremium = true
    )

    // Các mock data khác như trước
    val mockModule = mockModuleEntity.toDomainModel()
    val mockCreator = mockCreatorEntity.toDomainModel()
    val mockFlashcards = listOf(
        Flashcard(
            id = "flash1",
            term = "Cell",
            definition = "The basic unit of life",
            pronunciation = "sel",
            example = "Cells make up all living organisms",
            imageUrl = null,
            audioUrl = null,
            isStarred = false,
            lastReviewed = null,
            masteryLevel = 0
        ),
        Flashcard(
            id = "flash2",
            term = "DNA",
            definition = "Deoxyribonucleic acid, the molecule that carries genetic information",
            pronunciation = "dee-en-ay",
            example = "DNA is found in the nucleus of cells",
            imageUrl = null,
            audioUrl = null,
            isStarred = true,
            lastReviewed = System.currentTimeMillis() - 3600000, // 1 hour ago
            masteryLevel = 1
        ),
        Flashcard(
            id = "flash3",
            term = "Photosynthesis",
            definition = "The process by which plants convert light into energy",
            pronunciation = "foh-toh-sin-thuh-sis",
            example = "Photosynthesis occurs in chloroplasts",
            imageUrl = null,
            audioUrl = null,
            isStarred = false,
            lastReviewed = null,
            masteryLevel = 0
        )
    )


}