// File: app/src/main/java/com/kardio/features/library/domain/model/Folder.kt
package com.kardio.features.library.domain.model

data class Folder(
    val id: String,
    val name: String,
    val description: String = "",
    val moduleCount: Int = 0,
    val creatorName: String,
    val hasPlusBadge: Boolean = false
)