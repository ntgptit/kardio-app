// features/module/domain/model/Flashcard.kt
package com.kardio.features.module.domain.model

data class Flashcard(
    val id: String,
    val term: String,
    val definition: String,
    val pronunciation: String? = null,
    val example: String? = null,
    val imageUrl: String? = null,
    val audioUrl: String? = null,
    val isStarred: Boolean = false,
    val lastReviewed: Long? = null,
    val masteryLevel: Int = 0 // 0-3, where 0 is not learned, 3 is mastered
)