package com.kardio

import android.app.Application
import com.kardio.config.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KardioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Khởi tạo Timber hoặc các thư viện khác nếu cần
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Các khởi tạo khác của ứng dụng
    }
}