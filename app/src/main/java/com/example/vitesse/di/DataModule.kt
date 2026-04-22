package com.example.vitesse.di

import com.example.vitesse.data.network.CurrencyClient
import com.example.vitesse.data.repository.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideCurrencyRepository(dataClient: CurrencyClient): CurrencyRepository {
        return CurrencyRepository(dataClient)
    }
}
