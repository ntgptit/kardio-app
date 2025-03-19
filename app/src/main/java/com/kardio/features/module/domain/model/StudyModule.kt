// features/module/domain/model/StudyModule.kt
package com.kardio.features.module.domain.model

data class StudyModule(
    val id: String,
    val title: String,
    val description: String? = null,
    val creatorId: String,
    val creatorName: String,
    val lastUpdated: Long,
    val totalCards: Int,
    val isPublic: Boolean = true
)