package com.dev.james.domain.datasources.onboarding

import com.dev.james.booktracker.core.entities.UserDetailsEntity
import kotlinx.coroutines.flow.Flow

interface OnBoardingLocalDataSource {
    suspend fun storeOnBoardingStatus(status : Boolean)
    suspend fun getOnBoardingStatus() : Boolean
    suspend fun storeSelectedTheme(themeId : Int)

    fun getSelectedTheme() : Flow<Int>
    suspend fun saveUserDetails(userDetails : UserDetailsEntity)
    fun getUserDetails() : Flow<List<UserDetailsEntity>>
}