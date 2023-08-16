package com.dev.james.booktracker.home.data.datasource

import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_database.room.entities.BookGoalsEntity
import com.dev.james.booktracker.core_database.room.entities.OverallGoalEntity
import com.dev.james.booktracker.core_database.room.entities.SpecificGoalsEntity
import com.dev.james.booktracker.home.domain.datasources.GoalsLocalDataSource
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
}