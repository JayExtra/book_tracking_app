package com.dev.james.data.repositories.preferences

import com.dev.james.booktracker.core.user_preferences.mappers.toDomain
import com.dev.james.booktracker.core.common_models.UserDetails
import com.dev.james.booktracker.core_database.room.dao.CoreDao
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.domain.repository.main.UserPreferencesRepository
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