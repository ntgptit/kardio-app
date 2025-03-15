package com.kardio.features.dashboard.domain.repository

import com.kardio.features.dashboard.domain.model.DashboardData

interface DashboardRepository {
    suspend fun getDashboardData(): DashboardData
}