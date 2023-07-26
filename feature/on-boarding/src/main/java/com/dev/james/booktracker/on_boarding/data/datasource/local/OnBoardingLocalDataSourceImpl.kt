package com.dev.james.booktracker.on_boarding.data.datasource.local

import com.dev.james.booktracker.core_database.room.dao.OnBoardingDao
import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnBoardingLocalDataSourceImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val dao: OnBoardingDao
) : OnBoardingLocalDataSource {
    override suspend fun storeOnBoardingStatus(status: Boolean) {
      dataStoreManager.storeBooleanValue(
          DataStorePreferenceKeys.HAS_FINISHED_ON_BOARDING ,
          status
      )
    }

    override suspend fun getOnBoardingStatus(): Boolean {
       return dataStoreManager.readBooleanValueOnce(
           DataStorePreferenceKeys.HAS_FINISHED_ON_BOARDING
       )
    }

    override suspend fun storeSelectedTheme(themeId: Int) {
        dataStoreManager.storeIntValue(
            DataStorePreferenceKeys.CURRENT_THEME_SELECTED ,
            themeId
        )
    }

    override fun getSelectedTheme(): Flow<Int> {
        return dataStoreManager.readIntValueAsFlow(
            DataStorePreferenceKeys.CURRENT_THEME_SELECTED
        )
    }

    override suspend fun saveUserDetails(userDetails: UserDetailsEntity) {
        dao.addUserData(userDetails)
    }

    override fun getUserDetails(): Flow<List<UserDetailsEntity>> {
        return dao.getUserData()
    }
}