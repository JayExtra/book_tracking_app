package com.dev.james.booktracker.on_boarding.di

import com.dev.james.booktracker.core_database.room.dao.OnBoardingDao
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.on_boarding.data.datasource.OnBoardingLocalDataSource
import com.dev.james.booktracker.on_boarding.data.datasource.OnBoardingLocalDataSourceImpl
import com.dev.james.booktracker.on_boarding.data.repository.OnBoardingRepositoryImpl
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
    fun provideOnBoardingLocalDataSource(
        dataStoreManager: DataStoreManager ,
        dao: OnBoardingDao
    ) : OnBoardingLocalDataSource {
        return OnBoardingLocalDataSourceImpl(
            dataStoreManager ,
            dao
        )
    }

    @Provides
    @Singleton
    fun provideOnBoardingRepository(
        onBoardingLocalDataSource: OnBoardingLocalDataSource
    ) : OnBoardingRepository {
        return OnBoardingRepositoryImpl(onBoardingLocalDataSource)
    }

}