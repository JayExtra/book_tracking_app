package com.dev.james.booktracker.core.di

import com.dev.james.booktracker.core.user_preferences.data.UserPreferencesRepositoryImpl
import com.dev.james.booktracker.core.user_preferences.domain.UserPreferencesRepository
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideUserPreferenceRepo(
        dataStoreManager: DataStoreManager
    ) : UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(
            dataStoreManager
        )
    }
}