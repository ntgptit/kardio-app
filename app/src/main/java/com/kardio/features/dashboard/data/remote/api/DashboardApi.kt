package com.kardio.features.dashboard.data.remote.api

import com.kardio.features.dashboard.data.remote.model.DashboardResponse
import retrofit2.http.GET

interface DashboardApi {
    @GET("dashboard")
    suspend fun getDashboardData(): DashboardResponse
}