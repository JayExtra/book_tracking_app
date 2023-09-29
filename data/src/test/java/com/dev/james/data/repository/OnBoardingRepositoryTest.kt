package com.dev.james.data.repository

import com.dev.james.data.repositories.onboarding.OnBoardingRepositoryImpl
import com.dev.james.booktracker.core.common_models.UserDetails
import com.dev.james.domain.datasources.onboarding.OnBoardingLocalDataSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnBoardingRepositoryTest {
    private lateinit var repository : OnBoardingRepositoryImpl
    private lateinit var onBoardingLocalDataSource: OnBoardingLocalDataSource

    @Before
    fun setUp(){
        onBoardingLocalDataSource = com.dev.james.data.datasource.FakeOnBoardingLocalDataSource()
        repository = OnBoardingRepositoryImpl(
            onBoardingLocalDataSource = onBoardingLocalDataSource
        )
    }


    @Test
    fun `saving on boarding status with true returns true on boarding status`() = runTest {
        //given
        repository.updateOnBoardingStatus(true)
        //when
        val onBoardingStatus = repository.getOnBoardingStatus()

        //then
        assertThat(onBoardingStatus).isTrue()
    }

    @Test
    fun `saving user details in database returns user details in database`() = runTest {
        //given
        val userDetails = UserDetails(
            username = "Test Name" ,
            favouriteGenres = listOf("Genre1" , "Genre2" , "Genre 3") ,
            selectedAvatar = 1
        )

        //when
        repository.saveUserDetails(userDetails = userDetails)
        val currentUser = repository.getUserDetails().first()


        assertThat(currentUser[0].username).isEqualTo(userDetails.username)

    }

    @Test
    fun `saving current selected theme to database as 13 returns current selected theme as 13`() = runTest {
        //given
        repository.saveCurrentTheme(currentTheme = 13)

        //when
        val selectedTheme = repository.getSelectedTheme().first()

        assertThat(selectedTheme).isEqualTo(13)
    }




}