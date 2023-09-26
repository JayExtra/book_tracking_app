package com.dev.james.booktracker.on_boarding.data.repository

import com.dev.james.booktracker.core.user_preferences.mappers.toDomain
import com.dev.james.booktracker.core.user_preferences.mappers.toEntity
import com.dev.james.booktracker.on_boarding.data.datasource.local.OnBoardingLocalDataSource
import com.dev.james.booktracker.on_boarding.domain.OnBoardingRepository
import com.dev.james.booktracker.core.common_models.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getSelectedTheme(): Flow<Int> {
        return onBoardingLocalDataSource.getSelectedTheme()
    }
}