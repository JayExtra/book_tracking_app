package com.dev.james.on_boarding.data.datasource

import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import com.dev.james.booktracker.on_boarding.data.datasource.OnBoardingLocalDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeOnBoardingLocalDataSource : OnBoardingLocalDataSource {

    private var onBoardingStatus : Boolean = false
    private var selectedTheme : Int = 0
    private var fakeUserDetailsTable : MutableList<UserDetailsEntity> = mutableListOf()

    override suspend fun storeOnBoardingStatus(status: Boolean) {
        onBoardingStatus = status
    }

    override suspend fun getOnBoardingStatus(): Boolean {
       return onBoardingStatus
    }

    override suspend fun storeSelectedTheme(themeId: Int) {
       selectedTheme = themeId
    }

    override fun getSelectedTheme(): Flow<Int> = flow {
       emit(selectedTheme)
    }

    override suspend fun saveUserDetails(userDetails: UserDetailsEntity) {
        fakeUserDetailsTable.add(userDetails)
    }

    override fun getUserDetails(): Flow<List<UserDetailsEntity>> = flow {
       emit(fakeUserDetailsTable.toList())
    }
}