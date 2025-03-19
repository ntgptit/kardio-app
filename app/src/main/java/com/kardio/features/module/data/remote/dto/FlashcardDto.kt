// features/module/data/remote/dto/FlashcardDto.kt
package com.kardio.features.module.data.remote.dto

import com.kardio.features.module.domain.model.Flashcard
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlashcardDto(
    @Json(name = "id") val id: String,
    @Json(name = "term") val term: String,
    @Json(name = "definition") val definition: String,
    @Json(name = "pronunciation") val pronunciation: String?,
    @Json(name = "example") val example: String?,
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "audio_url") val audioUrl: String?,
    @Json(name = "is_starred") val isStarred: Boolean,
    @Json(name = "last_reviewed") val lastReviewed: Long?,
    @Json(name = "mastery_level") val masteryLevel: Int
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
}