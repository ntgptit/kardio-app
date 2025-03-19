// features/module/data/local/dao/ModuleDao.kt
package com.kardio.features.module.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kardio.features.module.data.local.entity.CreatorEntity
import com.kardio.features.module.data.local.entity.ModuleEntity

@Dao
interface ModuleDao {
    @Query("SELECT * FROM modules WHERE id = :moduleId")
    suspend fun getModuleById(moduleId: String): ModuleEntity?

    @Query("SELECT * FROM creators WHERE id = :creatorId")
    suspend fun getCreatorById(creatorId: String): CreatorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModule(module: ModuleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreator(creator: CreatorEntity)

    @Query("UPDATE modules SET lastOpenedTimestamp = :timestamp WHERE id = :moduleId")
    suspend fun updateLastOpenedTimestamp(moduleId: String, timestamp: Long)

    @Query("SELECT * FROM modules ORDER BY lastOpenedTimestamp DESC LIMIT :limit")
    suspend fun getRecentModules(limit: Int): List<ModuleEntity>
}