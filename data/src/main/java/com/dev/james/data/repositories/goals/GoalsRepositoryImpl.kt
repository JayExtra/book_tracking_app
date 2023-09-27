package com.dev.james.data.repositories.goals

import android.database.sqlite.SQLiteException
import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.common_models.OverallGoal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.common_models.mappers.mapToEntityObject
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.datasources.home.GoalsLocalDataSource
import com.dev.james.domain.repository.home.GoalsRepository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class GoalsRepositoryImpl @Inject constructor(
    private val goalsLocalDataSource: GoalsLocalDataSource,
    private val defaultDispatcher : CoroutineDispatcher = Dispatchers.IO
) : GoalsRepository {
    override suspend fun saveGoals(
        overallGoal: OverallGoal,
        specificGoal: SpecificGoal,
        bookGoal: BookGoal
    ): Resource<Boolean> {
        return try {
            coroutineScope {
                val overallGoalJob = launch { goalsLocalDataSource.addOverallGoalToDatabase(overallGoal.mapToEntityObject()) }
                val specificGoalJob = launch { goalsLocalDataSource.addSpecificGoalToDatabase(specificGoal.mapToEntityObject()) }
                val bookGoalJob = launch { goalsLocalDataSource.addBookGoalToDatabase(bookGoal.mapToEntityObject()) }

                joinAll(overallGoalJob , specificGoalJob , bookGoalJob)
            }
             Resource.Success(data = true)

        }catch (e : IOException){
             Resource.Error("Could not save goal to the database.Issue : ${e.message}")
        }catch ( e : SQLiteException){
            Resource.Error("Could not save goals to the database.Issue : ${e.message}")
        }
    }

    override suspend fun getAllActiveBookGoals(): List<BookGoal> =
            goalsLocalDataSource
                .getAllBookGoals()
                .filter { bookGoalsEntity ->
                    bookGoalsEntity.isActive
                }
                .map { bookGoalsEntity ->
                    bookGoalsEntity.toDomain()
                }


}