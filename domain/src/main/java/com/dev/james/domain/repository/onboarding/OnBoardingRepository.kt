package com.dev.james.domain.repository.onboarding

import com.dev.james.booktracker.core.common_models.UserDetails
import kotlinx.coroutines.flow.Flow

interface OnBoardingRepository {
    suspend fun updateOnBoardingStatus(status : Boolean)
    suspend fun getOnBoardingStatus() : Boolean
    fun getUserDetails() : Flow<List<UserDetails>>
    suspend fun saveUserDetails(userDetails: UserDetails)
    suspend fun saveCurrentTheme(currentTheme : Int)

    fun getSelectedTheme() : Flow<Int>

}