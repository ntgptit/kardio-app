// File: app/src/main/java/com/kardio/features/library/data/di/LibraryModule.kt
package com.kardio.features.library.data.di

import com.kardio.features.library.data.remote.api.LibraryApi
import com.kardio.features.library.data.repository.LibraryRepositoryImpl
import com.kardio.features.library.domain.repository.LibraryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LibraryModule {

    @Provides
    @Singleton
    fun provideLibraryApi(retrofit: Retrofit): LibraryApi {
        return retrofit.create(LibraryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLibraryRepository(
        libraryRepositoryImpl: LibraryRepositoryImpl
    ): LibraryRepository {
        return libraryRepositoryImpl
    }
}