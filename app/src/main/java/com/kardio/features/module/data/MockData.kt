import com.kardio.features.module.domain.model.Creator
import com.kardio.features.module.domain.model.Flashcard
import com.kardio.features.module.domain.model.StudyModule

// src/main/java/com/kardio/features/module/data/MockData.kt
object MockData {
    val mockModule = StudyModule(
        id = "module1",
        title = "Introduction to Biology",
        description = "Learn the basics of biology",
        creatorId = "creator1",
        creatorName = "John Doe",
        lastUpdated = System.currentTimeMillis() - 86400000, // 1 day ago
        totalCards = 5,
        isPublic = true
    )

    val mockCreator = Creator(
        id = "creator1",
        name = "John Doe",
        avatar = "https://example.com/avatar.jpg",
        isPremium = true
    )

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