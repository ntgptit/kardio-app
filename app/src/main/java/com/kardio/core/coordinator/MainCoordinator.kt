//package com.kardio.core.coordinator
//
//import com.kardio.core.di.IoDispatcher
//import com.kardio.features.dashboard.domain.model.DashboardData
//import com.kardio.features.dashboard.domain.repository.DashboardRepository
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.withContext
//import timber.log.Timber
//import javax.inject.Inject
//import javax.inject.Singleton
//
///**
// * Coordinator quản lý luồng dữ liệu và business logic giữa các feature
// */
//@Singleton
//class MainCoordinator @Inject constructor(
//    private val dashboardRepository: DashboardRepository,
//    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
//) {
//    /**
//     * Lấy dữ liệu dashboard từ repository
//     */
//    suspend fun getDashboardData(): DashboardData = withContext(ioDispatcher) {
//        Timber.d("Getting dashboard data via coordinator")
//        return@withContext dashboardRepository.getDashboardData()
//    }
//
//    /**
//     * Kiểm tra authenticated state của user
//     */
//    suspend fun isUserAuthenticated(): Boolean = withContext(ioDispatcher) {
//        // Ở đây sẽ gọi đến AuthRepository để kiểm tra
//        // Giả lập cho ví dụ là luôn authenticated
//        return@withContext true
//    }
//}