package com.kardio.features.dashboard.domain.usecase

import com.kardio.features.dashboard.domain.model.DashboardData
import com.kardio.features.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(): DashboardData {
        return dashboardRepository.getDashboardData()
    }
}