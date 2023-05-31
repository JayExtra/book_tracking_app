package com.dev.james.booktracker.on_boarding.data.datasource

import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import com.dev.james.booktracker.on_boarding.domain.models.UserDetails
import kotlinx.coroutines.flow.Flow

interface OnBoardingLocalDataSource {
    suspend fun storeOnBoardingStatus(status : Boolean)
    suspend fun getOnBoardingStatus() : Boolean
    suspend fun storeSelectedTheme(themeId : Int)

    fun getSelectedTheme() : Flow<Int>
    suspend fun saveUserDetails(userDetails : UserDetailsEntity)
    fun getUserDetails() : Flow<List<UserDetailsEntity>>
}