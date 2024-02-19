package com.dev.james.data.datasources.goals

import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.entities.updates.GoalUpdate
import com.dev.james.domain.datasources.home.GoalsLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GoalsLocalDataSourceImpl @Inject constructor(
    private val goalsDao: GoalsDao
) : GoalsLocalDataSource {
    override suspend fun addGoalToDatabase(goalEntity: GoalEntity) {
        goalsDao.addGoal(goalEntity)
    }

    override suspend fun getCachedGoalFromDatabase(goalId: String): GoalEntity {
        return goalsDao.getGoalById(goalId)
    }

    override suspend fun deleteCachedGoal(goalId: String) {
        goalsDao.deleteGoal(goalId)
    }

    override fun getCachedGoals(): Flow<List<GoalEntity>> {
        return goalsDao.getAllGoals()
    }

    override suspend fun updateBooksReadCount(goalUpdate: GoalUpdate) {
        goalsDao.updateBooksRead(goalUpdate)
    }


}