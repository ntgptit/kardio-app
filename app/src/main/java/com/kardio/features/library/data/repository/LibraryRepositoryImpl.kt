// File: app/src/main/java/com/kardio/features/library/data/repository/LibraryRepositoryImpl.kt
package com.kardio.features.library.data.repository

import com.kardio.core.di.IoDispatcher
import com.kardio.features.library.data.remote.api.LibraryApi
import com.kardio.features.library.domain.model.ClassModel
import com.kardio.features.library.domain.model.Folder
import com.kardio.features.library.domain.model.StudySet
import com.kardio.features.library.domain.repository.LibraryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepositoryImpl @Inject constructor(
    private val libraryApi: LibraryApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LibraryRepository {

    // In-memory cache
    private var studySetCache = mutableMapOf<String?, List<StudySet>>()
    private var folderCache = mutableListOf<Folder>()
    private var classCache = mutableListOf<ClassModel>()

    override suspend fun getStudySets(filter: String?, forceRefresh: Boolean): List<StudySet> = withContext(ioDispatcher) {
        // Check cache first if not forcing refresh
        if (!forceRefresh && studySetCache.containsKey(filter)) {
            return@withContext studySetCache[filter] ?: emptyList()
        }

        try {
            // In a real app, we would call the API
            // val response = libraryApi.getStudySets(filter)
            // For now, return mock data
            val result = getMockStudySets(filter)
            studySetCache[filter] = result
            return@withContext result
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getStudySetsByFolder(folderId: String, forceRefresh: Boolean): List<StudySet> = withContext(ioDispatcher) {
        // Cache key for folder study sets
        val cacheKey = "folder_$folderId"

        // Check cache first if not forcing refresh
        if (!forceRefresh && studySetCache.containsKey(cacheKey)) {
            return@withContext studySetCache[cacheKey] ?: emptyList()
        }

        try {
            // In a real app, we would call the API
            // val response = libraryApi.getStudySetsByFolder(folderId)
            // For now, return mock data
            val result = getMockStudySetsByFolder(folderId)
            studySetCache[cacheKey] = result
            return@withContext result
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getFolders(forceRefresh: Boolean): List<Folder> = withContext(ioDispatcher) {
        // Check cache first if not forcing refresh
        if (!forceRefresh && folderCache.isNotEmpty()) {
            return@withContext folderCache
        }

        try {
            // In a real app, we would call the API
            // val response = libraryApi.getFolders()
            // For now, return mock data
            val result = getMockFolders()
            folderCache.clear()
            folderCache.addAll(result)
            return@withContext result
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createFolder(name: String, description: String?): Folder = withContext(ioDispatcher) {
        try {
            // In a real app, we would call the API
            // val response = libraryApi.createFolder(name, description)
            // For now, create mock folder
            val newFolder = Folder(
                id = "new_folder_${System.currentTimeMillis()}",
                name = name,
                description = description ?: "",
                moduleCount = 0,
                creatorName = "Current User",
                hasPlusBadge = true
            )

            // Update cache
            folderCache.add(newFolder)

            return@withContext newFolder
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getClasses(forceRefresh: Boolean): List<ClassModel> = withContext(ioDispatcher) {
        // Check cache first if not forcing refresh
        if (!forceRefresh && classCache.isNotEmpty()) {
            return@withContext classCache
        }

        try {
            // In a real app, we would call the API
            // val response = libraryApi.getClasses()
            // For now, return mock data
            val result = getMockClasses()
            classCache.clear()
            classCache.addAll(result)
            return@withContext result
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createClass(name: String, description: String, allowMembersToAdd: Boolean): ClassModel = withContext(ioDispatcher) {
        try {
            // In a real app, we would call the API
            // val response = libraryApi.createClass(name, description, allowMembersToAdd)
            // For now, create mock class
            val newClass = ClassModel(
                id = "new_class_${System.currentTimeMillis()}",
                name = name,
                description = description,
                studyModulesCount = 0,
                creatorName = "Current User",
                allowMembersToAdd = allowMembersToAdd
            )

            // Update cache
            classCache.add(newClass)

            return@withContext newClass
        } catch (e: Exception) {
            throw e
        }
    }

    // Mock data methods for test purposes
    private fun getMockStudySets(filter: String?): List<StudySet> {
        val allStudySets = listOf(
            StudySet("1", "English Grammar: Verbs", 120, "teacherEnglish", true),
            StudySet("2", "Biology 101: Cell Structure", 45, "scienceTeacher", false),
            StudySet("3", "Spanish Vocabulary", 200, "languageMaster", true),
            StudySet("4", "Mathematics: Algebra", 75, "mathPro", false),
            StudySet("5", "History: World War II", 150, "historyBuff", true)
        )

        return when (filter) {
            "Của tôi" -> allStudySets.filter { it.creatorName == "Current User" }
            "Đã học" -> allStudySets.take(3)
            "Đã tải xuống" -> allStudySets.take(2)
            else -> allStudySets
        }
    }

    private fun getMockStudySetsByFolder(folderId: String): List<StudySet> {
        return when (folderId) {
            "1" -> listOf(
                StudySet("1", "English Grammar: Verbs", 120, "teacherEnglish", true),
                StudySet("3", "Spanish Vocabulary", 200, "languageMaster", true)
            )
            "2" -> listOf(
                StudySet("2", "Biology 101: Cell Structure", 45, "scienceTeacher", false),
                StudySet("4", "Mathematics: Algebra", 75, "mathPro", false)
            )
            else -> listOf(
                StudySet("5", "History: World War II", 150, "historyBuff", true)
            )
        }
    }

    private fun getMockFolders(): List<Folder> {
        return listOf(
            Folder("1", "Language Learning", "Folder for language learning materials", 10, "Current User", true),
            Folder("2", "Science", "Folder for science subjects", 5, "scienceTeacher", false),
            Folder("3", "History", "Folder for history materials", 7, "historyBuff", true)
        )
    }

    private fun getMockClasses(): List<ClassModel> {
        return listOf(
            ClassModel("1", "English Class 101", "Basic English class", 15, "teacherEnglish"),
            ClassModel("2", "Biology Advanced", "Advanced biology topics", 12, "scienceTeacher"),
            ClassModel("3", "History Club", "History discussion group", 8, "Current User")
        )
    }
}
