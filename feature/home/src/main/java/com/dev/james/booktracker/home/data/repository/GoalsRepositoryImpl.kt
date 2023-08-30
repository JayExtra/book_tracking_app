package com.dev.james.booktracker.home.data.repository

import android.database.sqlite.SQLiteException
import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.common_models.OverallGoal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.common_models.mappers.mapToEntityObject
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.home.domain.datasources.GoalsLocalDataSource
import com.dev.james.booktracker.home.domain.repositories.GoalsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class GoalsRepositoryImpl @Inject constructor(
    private val goalsLocalDataSource: GoalsLocalDataSource ,
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