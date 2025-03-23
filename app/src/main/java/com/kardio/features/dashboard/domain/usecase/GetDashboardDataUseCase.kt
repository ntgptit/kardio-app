package com.kardio.features.dashboard.domain.usecase

import com.kardio.features.dashboard.domain.model.DashboardData
import com.kardio.features.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * UseCase để lấy dữ liệu Dashboard
 * Tuân thủ Clean Architecture - domain layer không phụ thuộc vào data layer
 */
class GetDashboardDataUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * Execute use case để lấy dữ liệu dashboard
     * @return DashboardData domain model
     */
    suspend operator fun invoke(): DashboardData = withContext(ioDispatcher) {
        return@withContext dashboardRepository.getDashboardData()
    }
}