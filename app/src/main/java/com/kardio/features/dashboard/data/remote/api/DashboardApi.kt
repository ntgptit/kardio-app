package com.kardio.features.dashboard.data.remote.api

import com.kardio.features.dashboard.data.remote.model.DashboardResponse
import retrofit2.http.GET

/**
 * Retrofit API Service cho Dashboard
 */
interface DashboardApi {
    /**
     * Lấy dữ liệu dashboard từ server
     * @return DashboardResponse
     */
    @GET("dashboard")
    suspend fun getDashboardData(): DashboardResponse
}