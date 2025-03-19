// features/module/data/remote/dto/CreatorDto.kt
package com.kardio.features.module.data.remote.dto

import com.kardio.features.module.domain.model.Creator
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatorDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "avatar") val avatar: String?,
    @Json(name = "is_premium") val isPremium: Boolean
) {
    fun toDomainModel(): Creator {
        return Creator(
            id = id,
            name = name,
            avatar = avatar,
            isPremium = isPremium
        )
    }
}