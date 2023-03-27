package com.dev.james.booktracker.on_boarding.data.repository

import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.booktracker.on_boarding.data.datasource.OnBoardingLocalDataSource
import com.dev.james.booktracker.on_boarding.domain.OnBoardingRepository
import com.dev.james.booktracker.on_boarding.domain.mappers.toDomain
import com.dev.james.booktracker.on_boarding.domain.mappers.toEntity
import com.dev.james.booktracker.on_boarding.domain.models.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor(
    private val onBoardingLocalDataSource: OnBoardingLocalDataSource
) : OnBoardingRepository {
    override suspend fun updateOnBoardingStatus(status: Boolean) {
        onBoardingLocalDataSource.storeOnBoardingStatus(status)
    }

    override suspend fun getOnBoardingStatus(): Boolean {
       return onBoardingLocalDataSource.getOnBoardingStatus()
    }

    override fun getUserDetails(): Flow<List<UserDetails>> {
        return onBoardingLocalDataSource.getUserDetails()
            .map { userDetails ->
                userDetails.map {
                    it.toDomain()
                }
            }
    }

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        onBoardingLocalDataSource.saveUserDetails(userDetails.toEntity())
    }

    override suspend fun saveCurrentTheme(currentTheme: Int) {
        onBoardingLocalDataSource.storeSelectedTheme(
            currentTheme
        )
    }
}