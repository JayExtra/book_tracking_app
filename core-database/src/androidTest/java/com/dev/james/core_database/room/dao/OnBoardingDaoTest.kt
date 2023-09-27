package com.dev.james.core_database.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.booktracker.core.entities.UserDetailsEntity
import com.dev.james.booktracker.core_database.room.dao.OnBoardingDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@OptIn(ExperimentalCoroutinesApi::class)
class OnBoardingDaoTest {

    private lateinit var db : BookTrackerDatabase
    private lateinit var dao: OnBoardingDao

    @Before
    fun setup(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext() ,
            BookTrackerDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        dao = db.getOnBoardingDao()
    }

    @After
    fun tearDown(){
        db.close()
    }


    @Test
    fun insertUserDetails() = runTest {
        //given
        val userDetailsEntity = UserDetailsEntity(
            username = "Test Name",
            favouriteGenres = listOf("Genre1", "Genre2", "Genre 3"),
            selectedAvatar = 1
        )
        //when
        dao.addUserData(userDetailsEntity)

        //then
        val user = dao.getUserData().first()
        assertThat(user[0].username).isEqualTo(userDetailsEntity.username)
    }

}