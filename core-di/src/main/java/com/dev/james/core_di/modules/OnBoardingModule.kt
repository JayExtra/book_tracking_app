package com.dev.james.core_di.modules

import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.booktracker.core_database.room.dao.OnBoardingDao
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.domain.datasources.onboarding.OnBoardingLocalDataSource
import com.dev.james.data.repositories.onboarding.OnBoardingRepositoryImpl
import com.dev.james.domain.repository.onboarding.OnBoardingRepository
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
    fun provideOnBoardingDao(
        db : BookTrackerDatabase
    ) : OnBoardingDao {
        return  db.getOnBoardingDao()
    }

    @Provides
    @Singleton
    fun provideOnBoardingLocalDataSource(
        dataStoreManager: DataStoreManager ,
        dao: OnBoardingDao
    ) : OnBoardingLocalDataSource {
        return com.dev.james.data.datasources.onboarding.OnBoardingLocalDataSourceImpl(
            dataStoreManager,
            dao
        )
    }

    @Provides
    @Singleton
    fun provideOnBoardingRepository(
        onBoardingLocalDataSource: OnBoardingLocalDataSource
    ) : OnBoardingRepository {
        return OnBoardingRepositoryImpl(
            onBoardingLocalDataSource
        )
    }



}