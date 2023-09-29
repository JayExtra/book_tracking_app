package com.dev.james.core_di.modules

import android.content.Context
import com.dev.james.data.repositories.preferences.UserPreferencesRepositoryImpl
import com.dev.james.domain.repository.main.UserPreferencesRepository
import com.dev.james.domain.utilities.ConnectivityManager
import com.dev.james.data.utilities.ConnectivityManagerImpl
import com.dev.james.booktracker.core_database.room.dao.CoreDao
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
    fun provideUserPreferenceRepo(
        dataStoreManager: DataStoreManager ,
        dao: CoreDao
    ) : UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(
            dataStoreManager = dataStoreManager,
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