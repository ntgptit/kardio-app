// File: app/src/main/java/com/kardio/features/library/domain/repository/LibraryRepositoryExtensions.kt
package com.kardio.features.library.domain.repository

import com.kardio.features.library.domain.model.ClassDetail

/**
 * Các hàm extension cho LibraryRepository
 */

/**
 * Lấy thông tin chi tiết của một lớp học
 */
suspend fun LibraryRepository.getClassDetail(classId: String): ClassDetail {
    // Trong một ứng dụng thực tế, bạn sẽ gọi API để lấy thông tin chi tiết
    // Đây là một triển khai giả định dựa trên danh sách lớp học đã có
    val classes = getClasses()
    val classModel = classes.find { it.id == classId }
        ?: throw IllegalArgumentException("Không tìm thấy lớp học với ID: $classId")

    return ClassDetail(
        id = classModel.id,
        name = classModel.name,
        description = classModel.description,
        studyModulesCount = classModel.studyModulesCount,
        membersCount = 8, // Giả định có 8 thành viên
        creatorName = classModel.creatorName,
        creatorId = "creator_123", // Giả định ID của người tạo
        isCreatedByCurrentUser = classModel.creatorName == "Current User",
        allowMembersToAdd = classModel.allowMembersToAdd,
        hasJoined = true, // Giả định người dùng đã tham gia
        joinCode = "ABC123" // Giả định mã tham gia
    )
}

/**
 * Tham gia một lớp học
 */
suspend fun LibraryRepository.joinClass(classId: String): Boolean {
    // Giả định tham gia lớp học thành công
    return true
}

/**
 * Rời khỏi một lớp học
 */
suspend fun LibraryRepository.leaveClass(classId: String): Boolean {
    // Giả định rời khỏi lớp học thành công
    return true
}