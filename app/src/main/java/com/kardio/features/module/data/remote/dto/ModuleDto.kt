// features/module/data/remote/dto/ModuleDto.kt
package com.kardio.features.module.data.remote.dto

import com.kardio.features.module.domain.model.StudyModule
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModuleDto(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String?,
    @Json(name = "creator_id") val creatorId: String,
    @Json(name = "creator_name") val creatorName: String,
    @Json(name = "last_updated") val lastUpdated: Long,
    @Json(name = "total_cards") val totalCards: Int,
    @Json(name = "is_public") val isPublic: Boolean
) {
    fun toDomainModel(): StudyModule {
        return StudyModule(
            id = id,
            title = title,
            description = description,
            creatorId = creatorId,
            creatorName = creatorName,
            lastUpdated = lastUpdated,
            totalCards = totalCards,
            isPublic = isPublic
        )
    }
}