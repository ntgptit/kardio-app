
// features/module/data/local/dao/FlashcardDao.kt
package com.kardio.features.module.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kardio.features.module.data.local.entity.FlashcardEntity

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE moduleId = :moduleId")
    suspend fun getFlashcardsByModuleId(moduleId: String): List<FlashcardEntity>

    @Query("SELECT * FROM flashcards WHERE id = :flashcardId")
    suspend fun getFlashcardById(flashcardId: String): FlashcardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(flashcards: List<FlashcardEntity>)

    @Query("UPDATE flashcards SET isStarred = :isStarred WHERE id = :flashcardId")
    suspend fun updateFlashcardStarred(flashcardId: String, isStarred: Boolean)

    @Query("UPDATE flashcards SET masteryLevel = :level WHERE id = :flashcardId")
    suspend fun updateFlashcardMasteryLevel(flashcardId: String, level: Int)

    @Query("UPDATE flashcards SET lastReviewed = :timestamp WHERE id = :flashcardId")
    suspend fun updateFlashcardLastReviewed(flashcardId: String, timestamp: Long)
}