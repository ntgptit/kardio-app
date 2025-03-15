//// File: app/src/main/java/com/kardio/core/di/RepositoryModule.kt
//package com.kardio.core.di
//
//import com.kardio.features.cards.data.repository.CardsRepositoryImpl
//import com.kardio.features.cards.domain.repository.CardsRepository
//import dagger.Binds
//import dagger.Module
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
///**
// * Dagger Hilt module for binding repository implementations to their interfaces.
// */
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class RepositoryModule {
//
//    /**
//     * Binds CardsRepositoryImpl to CardsRepository interface.
//     */
//    @Binds
//    @Singleton
//    abstract fun bindCardsRepository(
//        cardsRepositoryImpl: CardsRepositoryImpl
//    ): CardsRepository
//
//    // Add more repository bindings as needed
//}
