package com.dev.james.booktracker.on_boarding.domain

import com.dev.james.booktracker.on_boarding.domain.models.UserDetails
import kotlinx.coroutines.flow.Flow

interface OnBoardingRepository {
    suspend fun updateOnBoardingStatus(status : Boolean)
    suspend fun getOnBoardingStatus() : Boolean
    fun getUserDetails() : Flow<List<UserDetails>>
    suspend fun saveUserDetails(userDetails: UserDetails)
    suspend fun saveCurrentTheme(currentTheme : Int)

    fun getSelectedTheme() : Flow<Int>

}