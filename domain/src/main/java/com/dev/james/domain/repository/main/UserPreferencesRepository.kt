package com.dev.james.domain.repository.main

import com.dev.james.booktracker.core.common_models.UserDetails
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getSelectedTheme() : Flow<Int>
    fun getOnBoardingStatus() : Flow<Boolean>
    fun getUserDetails() : Flow<UserDetails>

}