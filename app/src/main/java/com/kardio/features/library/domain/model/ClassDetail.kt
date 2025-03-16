// File: app/src/main/java/com/kardio/features/library/domain/model/ClassDetail.kt
package com.kardio.features.library.domain.model

data class ClassDetail(
    val id: String,
    val name: String,
    val description: String = "",
    val studyModulesCount: Int = 0,
    val membersCount: Int = 0,
    val creatorName: String? = null,
    val creatorId: String? = null,
    val isCreatedByCurrentUser: Boolean = false,
    val allowMembersToAdd: Boolean = true,
    val hasJoined: Boolean = false,
    val joinCode: String? = null
)