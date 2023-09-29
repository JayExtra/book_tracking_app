package com.dev.james.on_boarding.data

import com.dev.james.booktracker.core.user_preferences.mappers.toDomain
import com.dev.james.booktracker.core.user_preferences.mappers.toEntity
import com.dev.james.booktracker.core.entities.UserDetailsEntity
import com.dev.james.domain.repository.onboarding.OnBoardingRepository
import com.dev.james.booktracker.core.common_models.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeOnBoardingRepository : OnBoardingRepository {
    private var onBoardingStatus : Boolean = false
    private var selectedTheme : Int = 0
    private var fakeUserDetailsTable : MutableList<UserDetailsEntity> = mutableListOf()

    override suspend fun updateOnBoardingStatus(status: Boolean) {
       onBoardingStatus = status
    }

    override suspend fun getOnBoardingStatus(): Boolean {
       return onBoardingStatus
    }

    override fun getUserDetails(): Flow<List<UserDetails>> {
        return flow{ emit(fakeUserDetailsTable.map { it.toDomain() }.toList())}
    }

    override suspend fun saveUserDetails(userDetails: UserDetails) {
        fakeUserDetailsTable.add(userDetails.toEntity())
    }

    override suspend fun saveCurrentTheme(currentTheme: Int) {
        selectedTheme = currentTheme
    }

    override fun getSelectedTheme(): Flow<Int> {
        return flow { emit(selectedTheme) }
    }
}