package com.kardio.features.dashboard.domain.model

/**
 * Domain model cho thư mục
 */
data class Folder(
    val id: String,
    val name: String,
    val iconColor: Int,
    val createdByUsername: String,
    val isCreatedByPlusUser: Boolean
)

/**
 * Domain model cho học phần
 */
data class StudyModule(
    val id: String,
    val title: String,
    val termCount: Int,
    val createdByUsername: String,
    val isCreatedByPlusUser: Boolean
)

/**
 * Domain model cho lớp học
 */
data class Class(
    val id: String,
    val name: String,
    val studyModuleCount: Int,
    val memberCount: Int
)

/**
 * Domain model cho ngày trong chuỗi học
 */
data class StreakDay(
    val day: Int,
    val hasCompleted: Boolean
)

/**
 * Domain model cho dữ liệu chuỗi học
 */
data class StreakData(
    val currentStreak: Int,
    val weeklyStreak: List<StreakDay>
)

/**
 * Container cho tất cả dữ liệu dashboard
 */
data class DashboardData(
    val folders: List<Folder>,
    val studyModules: List<StudyModule>,
    val classes: List<Class>,
    val streakData: StreakData
)