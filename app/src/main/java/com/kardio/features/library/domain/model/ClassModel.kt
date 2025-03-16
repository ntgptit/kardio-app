// File: app/src/main/java/com/kardio/features/library/domain/model/ClassModel.kt
package com.kardio.features.library.domain.model

data class ClassModel(
    val id: String,
    val name: String,
    val description: String = "",
    val studyModulesCount: Int = 0,
    val creatorName: String? = null,
    val allowMembersToAdd: Boolean = true
)