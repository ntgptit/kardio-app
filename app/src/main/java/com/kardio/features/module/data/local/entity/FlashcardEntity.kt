// features/module/data/local/entity/FlashcardEntity.kt
package com.kardio.features.module.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kardio.features.module.domain.model.Flashcard

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = ModuleEntity::class,
            parentColumns = ["id"],
            childColumns = ["moduleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("moduleId")]
)
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val moduleId: String,
    val term: String,
    val definition: String,
    val pronunciation: String?,
    val example: String?,
    val imageUrl: String?,
    val audioUrl: String?,
    val isStarred: Boolean,
    val lastReviewed: Long?,
    val masteryLevel: Int
) {
    fun toDomainModel(): Flashcard {
        return Flashcard(
            id = id,
            term = term,
            definition = definition,
            pronunciation = pronunciation,
            example = example,
            imageUrl = imageUrl,
            audioUrl = audioUrl,
            isStarred = isStarred,
            lastReviewed = lastReviewed,
            masteryLevel = masteryLevel
        )
    }

    companion object {
        fun fromDomainModel(model: Flashcard, moduleId: String): FlashcardEntity {
            return FlashcardEntity(
                id = model.id,
                moduleId = moduleId,
                term = model.term,
                definition = model.definition,
                pronunciation = model.pronunciation,
                example = model.example,
                imageUrl = model.imageUrl,
                audioUrl = model.audioUrl,
                isStarred = model.isStarred,
                lastReviewed = model.lastReviewed,
                masteryLevel = model.masteryLevel
            )
        }
    }
}