package com.kardio.features.dashboard.data.repository

import com.kardio.features.dashboard.data.remote.api.DashboardApi
import com.kardio.features.dashboard.domain.model.Class
import com.kardio.features.dashboard.domain.model.DashboardData
import com.kardio.features.dashboard.domain.model.Folder
import com.kardio.features.dashboard.domain.model.StreakData
import com.kardio.features.dashboard.domain.model.StreakDay
import com.kardio.features.dashboard.domain.model.StudyModule
import com.kardio.features.dashboard.domain.repository.DashboardRepository
import com.kardio.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardApi: DashboardApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DashboardRepository {

    override suspend fun getDashboardData(): DashboardData = withContext(ioDispatcher) {
        try {
            // In a real app, we would fetch this from the API and map response to domain models
            // For demo, return mock data
            return@withContext createMockDashboardData()
        } catch (e: Exception) {
            throw Exception("Failed to fetch dashboard data: ${e.message}")
        }
    }

    // Mock data creation (in a real app this would come from the API)
    private fun createMockDashboardData(): DashboardData {
        val folders = listOf(
            Folder(
                id = "folder1",
                name = "Grammar",
                iconColor = 0xFF4255FF.toInt(), // Accent color
                createdByUsername = "giapnguyen1994",
                isCreatedByPlusUser = true
            ),
            Folder(
                id = "folder2",
                name = "Others",
                iconColor = 0xFF2196F3.toInt(), // Secondary color
                createdByUsername = "giapnguyen1994",
                isCreatedByPlusUser = true
            )
        )

        val studyModules = listOf(
            StudyModule(
                id = "module1",
                title = "Vitamin_Book1_Chapter1-3: Double Fin...",
                termCount = 12,
                createdByUsername = "giapnguyen1994",
                isCreatedByPlusUser = true
            ),
            StudyModule(
                id = "module2",
                title = "English Grammar: Tenses",
                termCount = 20,
                createdByUsername = "giapnguyen1994",
                isCreatedByPlusUser = true
            )
        )

        val classes = listOf(
            Class(
                id = "class1",
                name = "Korean_Multicampus",
                studyModuleCount = 56,
                memberCount = 8
            )
        )

        val weeklyStreak = listOf(
            StreakDay(day = 9, hasCompleted = false),
            StreakDay(day = 10, hasCompleted = true),
            StreakDay(day = 11, hasCompleted = false),
            StreakDay(day = 12, hasCompleted = true),
            StreakDay(day = 13, hasCompleted = true),
            StreakDay(day = 14, hasCompleted = false),
            StreakDay(day = 15, hasCompleted = false)
        )

        val streakData = StreakData(
            currentStreak = 14,
            weeklyStreak = weeklyStreak
        )

        return DashboardData(
            folders = folders,
            studyModules = studyModules,
            classes = classes,
            streakData = streakData
        )
    }
}