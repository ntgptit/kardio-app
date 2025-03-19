// features/module/di/ModuleModule.kt
package com.kardio.features.module.di

import com.kardio.core.di.IoDispatcher
import com.kardio.features.module.data.local.dao.FlashcardDao
import com.kardio.features.module.data.local.dao.MockFlashcardDao
import com.kardio.features.module.data.local.dao.MockModuleDao
import com.kardio.features.module.data.local.dao.ModuleDao
import com.kardio.features.module.data.local.datasource.ModuleLocalDataSource
import com.kardio.features.module.data.remote.api.ModuleApiService
import com.kardio.features.module.data.remote.datasource.ModuleRemoteDataSource
import com.kardio.features.module.data.repository.ModuleRepositoryImpl
import com.kardio.features.module.domain.repository.ModuleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleModule {

    @Provides
    @Singleton
    fun provideModuleApiService(retrofit: Retrofit): ModuleApiService {
        return retrofit.create(ModuleApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideModuleRemoteDataSource(
        moduleApiService: ModuleApiService,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ModuleRemoteDataSource {
        return ModuleRemoteDataSource(moduleApiService, dispatcher)
    }

    @Provides
    @Singleton
    fun provideModuleDao(): ModuleDao {
        return MockModuleDao() // Đã cung cấp trước đó
    }

    @Provides
    @Singleton
    fun provideFlashcardDao(): FlashcardDao {
        return MockFlashcardDao() // Thêm mock FlashcardDao
    }

    @Provides
    @Singleton
    fun provideModuleLocalDataSource(
        moduleDao: ModuleDao,
        flashcardDao: FlashcardDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ModuleLocalDataSource {
        return ModuleLocalDataSource(moduleDao, flashcardDao, dispatcher)
    }

    @Provides
    @Singleton
    fun provideModuleRepository(
        remoteDataSource: ModuleRemoteDataSource,
        localDataSource: ModuleLocalDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ModuleRepository {
        return ModuleRepositoryImpl(remoteDataSource, localDataSource, dispatcher)
    }
}