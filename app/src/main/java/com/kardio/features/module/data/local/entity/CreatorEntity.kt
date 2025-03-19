// features/module/data/local/entity/CreatorEntity.kt
package com.kardio.features.module.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kardio.features.module.domain.model.Creator

@Entity(tableName = "creators")
data class CreatorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val avatar: String?,
    val isPremium: Boolean
) {
    fun toDomainModel(): Creator {
        return Creator(
            id = id,
            name = name,
            avatar = avatar,
            isPremium = isPremium
        )
    }

    companion object {
        fun fromDomainModel(model: Creator): CreatorEntity {
            return CreatorEntity(
                id = model.id,
                name = model.name,
                avatar = model.avatar,
                isPremium = model.isPremium
            )
        }
    }
}