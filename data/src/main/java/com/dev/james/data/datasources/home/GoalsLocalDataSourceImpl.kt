package com.dev.james.data.datasources.home

import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core.entities.BookGoalsEntity
import com.dev.james.booktracker.core.entities.OverallGoalEntity
import com.dev.james.booktracker.core.entities.SpecificGoalsEntity
import com.dev.james.domain.datasources.home.GoalsLocalDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GoalsLocalDataSourceImpl @Inject constructor(
    private val goalsDao: GoalsDao
) : GoalsLocalDataSource {
    override suspend fun addOverallGoalToDatabase(overallGoalEntity: OverallGoalEntity) {
        goalsDao.addOverallGoal(overallGoalEntity)
    }

    override suspend fun addSpecificGoalToDatabase(specificGoalsEntity: SpecificGoalsEntity) {
        goalsDao.addSpecificGoal(specificGoalsEntity)
    }

    override suspend fun addBookGoalToDatabase(bookGoalsEntity: BookGoalsEntity) {
        goalsDao.addBookGoal(bookGoalsEntity)
    }

    override suspend fun getAllBookGoals(): List<BookGoalsEntity> {
        return goalsDao.getAllBookGoals().first()
    }

}