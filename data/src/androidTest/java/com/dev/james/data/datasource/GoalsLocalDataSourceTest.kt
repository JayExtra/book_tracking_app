package com.dev.james.data.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dev.james.booktracker.core.test_commons.getTestGoalEntity
import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.data.datasources.goals.GoalsLocalDataSourceImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GoalsLocalDataSourceTest {
    private lateinit var bookTrackerDatabase: BookTrackerDatabase
    private lateinit var goalsDao: GoalsDao
    private lateinit var goalsLocalDataSourceImpl: GoalsLocalDataSourceImpl



    @Before
    fun setUp(){
        bookTrackerDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext() ,
            BookTrackerDatabase::class.java
        ).allowMainThreadQueries().build()

        goalsDao = bookTrackerDatabase.getGoalsDao()

        goalsLocalDataSourceImpl = GoalsLocalDataSourceImpl(
            goalsDao = goalsDao
        )

    }

    @After
    fun tearDown(){
        bookTrackerDatabase.close()
    }

    @Test
    fun insertOverallGoal_expectedOneOverallGoalInserted() = runTest {
        //given
        val testGoalEntity = getTestGoalEntity()
        //when
        goalsLocalDataSourceImpl.addGoalToDatabase(testGoalEntity)
        val addedOverallGoal = goalsDao.getGoalById(testGoalEntity.id)
        //then
        assertThat(addedOverallGoal.id).isEqualTo(testGoalEntity.id)

    }


}