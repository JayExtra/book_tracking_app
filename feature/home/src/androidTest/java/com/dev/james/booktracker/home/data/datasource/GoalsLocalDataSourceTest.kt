package com.dev.james.booktracker.home.data.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dev.james.booktracker.core.test_commons.getTestBookGoalEntity
import com.dev.james.booktracker.core.test_commons.getTestOverallGoalEntity
import com.dev.james.booktracker.core.test_commons.getTestSpecificGoalEntity
import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
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
        val overallGoalEntity = getTestOverallGoalEntity()
        //when
        goalsLocalDataSourceImpl.addOverallGoalToDatabase(overallGoalEntity)
        val addedOverallGoal = goalsDao.getOverallGoal(overallGoalEntity.goalId)
        //then
        assertThat(addedOverallGoal.goalId).isEqualTo(overallGoalEntity.goalId)

    }

    @Test
    fun insertSpecificGoal_expectedOneSpecificGoalInserted() = runTest {
        //given
        val specificGoalsEntity = getTestSpecificGoalEntity()
        //when
        goalsLocalDataSourceImpl.addSpecificGoalToDatabase(specificGoalsEntity)
        val addedSpecificGoal = goalsDao.getSingleSpecificGoal(goalId = specificGoalsEntity.goalId)
        //then
        assertThat(addedSpecificGoal.goalId).isEqualTo(specificGoalsEntity.goalId)
    }

    @Test
    fun insertBookGoal_expectedOneBookGoalInserted() = runTest {
        //given
        val bookGoalEntity = getTestBookGoalEntity()
        //when
        goalsLocalDataSourceImpl.addBookGoalToDatabase(bookGoalEntity)
        val addedBookGoal = goalsDao.getBookGoal(bookGoalEntity.bookId)
        //then
        assertThat(addedBookGoal.bookId).isEqualTo(bookGoalEntity.bookId)
    }

}