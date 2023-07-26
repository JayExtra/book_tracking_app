package com.dev.james.booktracker.core.user_preferences.domain.repo

import com.dev.james.booktracker.core.user_preferences.data.models.UserDetails
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getSelectedTheme() : Flow<Int>
    fun getOnBoardingStatus() : Flow<Boolean>
    fun getUserDetails() : Flow<UserDetails>

}