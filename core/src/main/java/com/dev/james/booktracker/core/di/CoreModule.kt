package com.dev.james.booktracker.core.di

import android.content.Context
import com.dev.james.booktracker.core.user_preferences.data.repo.UserPreferencesRepositoryImpl
import com.dev.james.booktracker.core.user_preferences.domain.repo.UserPreferencesRepository
import com.dev.james.booktracker.core.utilities.ConnectivityManager
import com.dev.james.booktracker.core.utilities.ConnectivityManagerImpl
import com.dev.james.booktracker.core_database.room.dao.CoreDao
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideCoreDao(
        db : BookTrackerDatabase
    ) : CoreDao {
        return db.getCoreDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferenceRepo(
        dataStoreManager: DataStoreManager ,
        dao: CoreDao
    ) : UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(
            dataStoreManager = dataStoreManager ,
            dao = dao
        )
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context  : Context
    ) : ConnectivityManager {
        return ConnectivityManagerImpl(
            context
        )
    }
}