package com.dev.james.booktracker.core.user_preferences.data

import com.dev.james.booktracker.core.user_preferences.domain.UserPreferencesRepository
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : UserPreferencesRepository {
    override fun getSelectedTheme(): Flow<Int> {
        return dataStoreManager.getSelectedThemeStream(
            DataStorePreferenceKeys.CURRENT_THEME_SELECTED
        )
    }
    override suspend fun getOnBoardingStatus(): Boolean {
        return dataStoreManager.readBooleanValueOnce(
            DataStorePreferenceKeys.HAS_FINISHED_ON_BOARDING
        )
    }
}