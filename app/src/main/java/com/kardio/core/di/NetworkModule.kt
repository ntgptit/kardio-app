//// File: app/src/main/java/com/kardio/core/di/NetworkModule.kt
//package com.kardio.core.di
//
//import com.kardio.BuildConfig
//import com.kardio.network.interceptor.AuthInterceptor
//import com.kardio.network.interceptor.NetworkConnectionInterceptor
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.moshi.MoshiConverterFactory
//import java.util.concurrent.TimeUnit
//import javax.inject.Singleton
//
///**
// * Dagger Hilt module for providing network-related dependencies.
// */
//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//
//    private const val TIMEOUT_SECONDS = 30L
//
//    /**
//     * Provides HttpLoggingInterceptor for logging network requests in debug builds.
//     */
//    @Provides
//    @Singleton
//    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
//        return HttpLoggingInterceptor().apply {
//            level = if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor.Level.BODY
//            } else {
//                HttpLoggingInterceptor.Level.NONE
//            }
//        }
//    }
//
//    /**
//     * Provides NetworkConnectionInterceptor for checking network connectivity.
//     */
//    @Provides
//    @Singleton
//    fun provideNetworkConnectionInterceptor(
//        // Context would be provided by Hilt
//    ): NetworkConnectionInterceptor {
//        return NetworkConnectionInterceptor()
//    }
//
//    /**
//     * Provides AuthInterceptor for adding authentication headers to requests.
//     */
//    @Provides
//    @Singleton
//    fun provideAuthInterceptor(
//        // Auth dependencies would be provided by Hilt
//    ): AuthInterceptor {
//        return AuthInterceptor()
//    }
//
//    /**
//     * Provides OkHttpClient configured with all necessary interceptors and timeouts.
//     */
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(
//        loggingInterceptor: HttpLoggingInterceptor,
//        networkConnectionInterceptor: NetworkConnectionInterceptor,
//        authInterceptor: AuthInterceptor
//    ): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(networkConnectionInterceptor)
//            .addInterceptor(authInterceptor)
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
//            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
//            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
//            .build()
//    }
//
//    /**
//     * Provides Retrofit instance configured with Moshi converter and base URL.
//     */
//    @Provides
//    @Singleton
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BuildConfig.API_BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(MoshiConverterFactory.create())
//            .build()
//    }
//}