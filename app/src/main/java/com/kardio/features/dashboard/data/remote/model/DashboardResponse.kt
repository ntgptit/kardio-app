package com.kardio.features.dashboard.data.remote.model

data class DashboardResponse(
    val folders: List<FolderDto> = emptyList(),
    val studyModules: List<StudyModuleDto> = emptyList(),
    val classes: List<ClassDto> = emptyList(),
    val streakData: StreakDataDto? = null
)

data class FolderDto(
    val id: String,
    val name: String,
    val iconColor: String,
    val createdBy: UserDto
)

data class StudyModuleDto(
    val id: String,
    val title: String,
    val termCount: Int,
    val createdBy: UserDto
)

data class ClassDto(
    val id: String,
    val name: String,
    val studyModuleCount: Int,
    val memberCount: Int
)

data class UserDto(
    val username: String,
    val isPlusUser: Boolean
)

data class StreakDayDto(
    val day: Int,
    val hasCompleted: Boolean
)

data class StreakDataDto(
    val currentStreak: Int,
    val weeklyStreak: List<StreakDayDto>
)