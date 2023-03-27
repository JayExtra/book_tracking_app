package com.dev.james.booktracker.core.user_preferences.domain

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getSelectedTheme() : Flow<Int>
    suspend fun getOnBoardingStatus() : Boolean
}