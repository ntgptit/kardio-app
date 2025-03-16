//package com.kardio.core.di
//
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.Dispatchers
//import javax.inject.Qualifier
//import javax.inject.Singleton
//
///**
// * Dagger Hilt module cung cấp phụ thuộc cho toàn bộ ứng dụng
// */
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    /**
//     * Cung cấp Main dispatcher cho UI operations
//     */
//    @MainDispatcher
//    @Provides
//    @Singleton
//    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
//
//    /**
//     * Cung cấp IO dispatcher cho I/O operations (network, database)
//     */
//    @IoDispatcher
//    @Provides
//    @Singleton
//    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
//
//    /**
//     * Cung cấp Default dispatcher cho CPU-intensive tasks
//     */
//    @DefaultDispatcher
//    @Provides
//    @Singleton
//    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
//}
