// File: app/src/main/java/com/kardio/features/library/domain/model/StudySet.kt
package com.kardio.features.library.domain.model

data class StudySet(
    val id: String,
    val title: String,
    val wordCount: Int,
    val creatorName: String,
    val hasPlusBadge: Boolean
)