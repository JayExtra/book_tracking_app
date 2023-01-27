package com.dev.james.booktracker.on_boarding.di

import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.on_boarding.data.OnBoardingRepositoryImpl
import com.dev.james.booktracker.on_boarding.domain.OnBoardingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnBoardingModule {

    @Provides
    @Singleton
    fun provideOnBoardingRepository(
        dataStoreManager: DataStoreManager
    ) : OnBoardingRepository {
        return OnBoardingRepositoryImpl(dataStoreManager)
    }

}