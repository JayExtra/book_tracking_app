package com.dev.james.booktracker.data


import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.booktracker.domain.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : MainRepository {
    override suspend fun getOnBoardingStatus(): Boolean = dataStoreManager.readBooleanValueOnce(
        DataStorePreferenceKeys.HAS_FINISHED_ON_BOARDING
    )
}