//// File: app/src/main/java/com/kardio/core/di/UseCaseModule.kt
//package com.kardio.core.di
//
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import com.kardio.features.cards.domain.repository.CardsRepository
//import com.kardio.features.cards.domain.usecase.GetCardsUseCase
//import javax.inject.Singleton
//
///**
// * Dagger Hilt module for providing use case instances.
// */
//@Module
//@InstallIn(SingletonComponent::class)
//object UseCaseModule {
//
//    /**
//     * Provides GetCardsUseCase instance.
//     */
//    @Provides
//    @Singleton
//    fun provideGetCardsUseCase(
//        cardsRepository: CardsRepository,
//        @IoDispatcher ioDispatcher: kotlinx.coroutines.CoroutineDispatcher
//    ): GetCardsUseCase {
//        return GetCardsUseCase(cardsRepository, ioDispatcher)
//    }
//
//    // Add more use case providers as needed
//}