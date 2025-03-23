package com.kardio.features.dashboard.domain.repository

import com.kardio.features.dashboard.domain.model.DashboardData

/**
 * Repository interface cho dashboard
 * Tuân thủ Clean Architecture - domain layer định nghĩa interface, data layer triển khai
 */
interface DashboardRepository {
    /**
     * Lấy dữ liệu dashboard
     * @return DashboardData domain model
     */
    suspend fun getDashboardData(): DashboardData
}