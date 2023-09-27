package com.dev.james.booktracker.feature.on_boarding.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dev.james.booktracker.core.test_commons.FakeDataStoreManager
import com.dev.james.booktracker.core_database.room.dao.OnBoardingDao
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.domain.datasources.onboarding.OnBoardingLocalDataSource
import com.dev.james.data.datasources.onboarding.OnBoardingLocalDataSourceImpl
import com.dev.james.data.repositories.onboarding.OnBoardingRepositoryImpl
import com.dev.james.domain.repository.onboarding.OnBoardingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestOnBoardingModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) : BookTrackerDatabase {
        return Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext() ,
            BookTrackerDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(
        db : BookTrackerDatabase
    ) : OnBoardingDao {
        return db.getOnBoardingDao()
    }

    @Provides
    @Singleton
    fun provideDataStoreManager() : DataStoreManager {
        return FakeDataStoreManager()
    }

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