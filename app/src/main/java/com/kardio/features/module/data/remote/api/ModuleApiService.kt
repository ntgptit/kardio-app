// features/module/data/remote/api/ModuleApiService.kt
package com.kardio.features.module.data.remote.api

import com.kardio.features.module.data.remote.dto.CreatorDto
import com.kardio.features.module.data.remote.dto.FlashcardDto
import com.kardio.features.module.data.remote.dto.ModuleDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ModuleApiService {
    @GET("modules/{moduleId}")
    suspend fun getModuleById(@Path("moduleId") moduleId: String): Response<ModuleDto>

    @GET("modules/{moduleId}/flashcards")
    suspend fun getFlashcardsByModuleId(@Path("moduleId") moduleId: String): Response<List<FlashcardDto>>

    @GET("creators/{creatorId}")
    suspend fun getCreatorById(@Path("creatorId") creatorId: String): Response<CreatorDto>

    @POST("modules/{moduleId}/last-opened")
    suspend fun updateLastOpenedTimestamp(
        @Path("moduleId") moduleId: String,
        @Query("timestamp") timestamp: Long
    ): Response<Unit>

    @POST("flashcards/{flashcardId}/star")
    suspend fun updateFlashcardStarred(
        @Path("flashcardId") flashcardId: String,
        @Query("isStarred") isStarred: Boolean
    ): Response<Boolean>

    @POST("flashcards/{flashcardId}/mastery")
    suspend fun updateFlashcardMasteryLevel(
        @Path("flashcardId") flashcardId: String,
        @Query("level") level: Int
    ): Response<Int>
}