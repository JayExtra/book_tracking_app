package com.dev.james.booktracker.on_boarding.data

import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.booktracker.on_boarding.domain.OnBoardingRepository
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor(
    private val dataStoreManager : DataStoreManager
) : OnBoardingRepository {
    override suspend fun updateOnBoardingStatus(status: Boolean) {
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
}