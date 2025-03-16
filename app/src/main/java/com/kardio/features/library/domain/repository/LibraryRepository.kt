// File: app/src/main/java/com/kardio/features/library/domain/repository/LibraryRepository.kt
package com.kardio.features.library.domain.repository

import com.kardio.features.library.domain.model.ClassModel
import com.kardio.features.library.domain.model.Folder
import com.kardio.features.library.domain.model.StudySet

interface LibraryRepository {
    // StudySets
    suspend fun getStudySets(filter: String? = null, forceRefresh: Boolean = false): List<StudySet>
    suspend fun getStudySetsByFolder(folderId: String, forceRefresh: Boolean = false): List<StudySet>

    // Folders
    suspend fun getFolders(forceRefresh: Boolean = false): List<Folder>
    suspend fun createFolder(name: String, description: String? = null): Folder

    // Classes
    suspend fun getClasses(forceRefresh: Boolean = false): List<ClassModel>
    suspend fun createClass(name: String, description: String, allowMembersToAdd: Boolean): ClassModel
}