//// File: app/src/main/java/com/kardio/core/di/DatabaseModule.kt
//package com.kardio.core.di
//
//import android.app.Application
//import androidx.room.Room
//import com.kardio.database.KardioDatabase
//import com.kardio.database.dao.CardDao
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
///**
// * Dagger Hilt module for providing database-related dependencies.
// */
//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//
//    private const val DATABASE_NAME = "kardio_database"
//
//    /**
//     * Provides Room database instance.
//     */
//    @Provides
//    @Singleton
//    fun provideDatabase(application: Application): KardioDatabase {
//        return Room.databaseBuilder(
//            application,
//            KardioDatabase::class.java,
//            DATABASE_NAME
//        )
//            .fallbackToDestructiveMigration() // In a real app, you would want to handle migrations
//            .build()
//    }
//
//    /**
//     * Provides CardDao for accessing card data in the database.
//     */
//    @Provides
//    @Singleton
//    fun provideCardDao(database: KardioDatabase): CardDao {
//        return database.cardDao()
//    }
//
//    // Add more DAO providers as needed
//}
