// features/module/data/local/entity/ModuleEntity.kt
package com.kardio.features.module.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kardio.features.module.domain.model.StudyModule

@Entity(tableName = "modules")
data class ModuleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val creatorId: String,
    val creatorName: String,
    val lastUpdated: Long,
    val totalCards: Int,
    val isPublic: Boolean,
    val lastOpenedTimestamp: Long? = null
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

    companion object {
        fun fromDomainModel(model: StudyModule): ModuleEntity {
            return ModuleEntity(
                id = model.id,
                title = model.title,
                description = model.description,
                creatorId = model.creatorId,
                creatorName = model.creatorName,
                lastUpdated = model.lastUpdated,
                totalCards = model.totalCards,
                isPublic = model.isPublic
            )
        }
    }
}