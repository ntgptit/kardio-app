// File: app/src/main/java/com/kardio/features/library/data/remote/api/LibraryApi.kt
package com.kardio.features.library.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LibraryApi {
    @GET("study-sets")
    suspend fun getStudySets(@Query("filter") filter: String? = null): List<StudySetDto>

    @GET("folders/{folderId}/study-sets")
    suspend fun getStudySetsByFolder(@Path("folderId") folderId: String): List<StudySetDto>

    @GET("folders")
    suspend fun getFolders(): List<FolderDto>

    @POST("folders")
    suspend fun createFolder(@Body request: CreateFolderRequest): FolderDto

    @GET("classes")
    suspend fun getClasses(): List<ClassDto>

    @POST("classes")
    suspend fun createClass(@Body request: CreateClassRequest): ClassDto
}

// Data Transfer Objects
data class StudySetDto(
    val id: String,
    val title: String,
    val wordCount: Int,
    val creatorName: String,
    val hasPlusBadge: Boolean
)

data class FolderDto(
    val id: String,
    val name: String,
    val description: String,
    val moduleCount: Int,
    val creatorName: String,
    val hasPlusBadge: Boolean
)

data class ClassDto(
    val id: String,
    val name: String,
    val description: String,
    val studyModulesCount: Int,
    val creatorName: String?,
    val allowMembersToAdd: Boolean
)

data class CreateFolderRequest(
    val name: String,
    val description: String?
)

data class CreateClassRequest(
    val name: String,
    val description: String,
    val allowMembersToAdd: Boolean
)