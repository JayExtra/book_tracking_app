package com.dev.james.booktracker.core.user_preferences.data.repo

import com.dev.james.booktracker.core.user_preferences.data.mappers.toDomain
import com.dev.james.booktracker.core.user_preferences.data.models.UserDetails
import com.dev.james.booktracker.core.user_preferences.domain.repo.UserPreferencesRepository
import com.dev.james.booktracker.core_database.room.dao.CoreDao
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager ,
    private val dao : CoreDao
) : UserPreferencesRepository {
    override fun getSelectedTheme(): Flow<Int> {
        return dataStoreManager.getSelectedThemeStream(
            DataStorePreferenceKeys.CURRENT_THEME_SELECTED
        )
    }
    override fun getOnBoardingStatus(): Flow<Boolean> {
        return dataStoreManager.readBooleanValueAsFlow(
            DataStorePreferenceKeys.HAS_FINISHED_ON_BOARDING
        )
    }
    override fun getUserDetails(): Flow<UserDetails> {
        val userInfo = dao.getUserInformation()
            .filter {
                it.isNotEmpty()
            }.map {
                it[0].toDomain()
            }

        return userInfo

    }
}