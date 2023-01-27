package com.dev.james.booktracker.di

import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.data.MainRepositoryImpl
import com.dev.james.booktracker.domain.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        dataStoreManager: DataStoreManager
    ) : MainRepository {
        return MainRepositoryImpl(dataStoreManager)
    }
}