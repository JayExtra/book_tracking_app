package com.dev.james.domain.datasources.home

import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.entities.updates.GoalUpdate
import kotlinx.coroutines.flow.Flow

interface GoalsLocalDataSource {

    suspend fun addGoalToDatabase(goalEntity : GoalEntity)

    suspend fun getCachedGoalFromDatabase(goalId : String): GoalEntity

    suspend fun deleteCachedGoal(goalId: String)

    fun getCachedGoals() : Flow<List<GoalEntity>>

    suspend fun updateBooksReadCount(goalUpdate: GoalUpdate)

}