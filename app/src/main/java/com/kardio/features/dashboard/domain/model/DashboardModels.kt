package com.kardio.features.dashboard.domain.model

data class Folder(
    val id: String,
    val name: String,
    val iconColor: Int,
    val createdByUsername: String,
    val isCreatedByPlusUser: Boolean
)

data class StudyModule(
    val id: String,
    val title: String,
    val termCount: Int,
    val createdByUsername: String,
    val isCreatedByPlusUser: Boolean
)

data class Class(
    val id: String,
    val name: String,
    val studyModuleCount: Int,
    val memberCount: Int
)

data class StreakDay(
    val day: Int,
    val hasCompleted: Boolean
)

data class StreakData(
    val currentStreak: Int,
    val weeklyStreak: List<StreakDay>
)

// Container for all dashboard data
data class DashboardData(
    val folders: List<Folder>,
    val studyModules: List<StudyModule>,
    val classes: List<Class>,
    val streakData: StreakData
)