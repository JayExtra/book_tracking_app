package com.dev.james.data.repositories.goals

import android.database.sqlite.SQLiteException
import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.common_models.mappers.mapToEntityObject
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.entities.updates.GoalUpdate
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.domain.datasources.home.GoalsLocalDataSource
import com.dev.james.domain.repository.home.GoalsRepository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class GoalsRepositoryImpl @Inject constructor(
    private val goalsLocalDataSource: GoalsLocalDataSource,
    private val dataStoreManager: DataStoreManager,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GoalsRepository {

    companion object {
        const val TAG = "GoalsRepositoryImpl"
    }

    override suspend fun saveGoals(
        goal: Goal
    ): Resource<Boolean> {
        return try {
            goalsLocalDataSource.addGoalToDatabase(goal.mapToEntityObject())
            dataStoreManager.storeStringValue(
                DataStorePreferenceKeys.CURRENT_ACTIVE_GOAL_ID,
                goal.goalId
            )
            Resource.Success(data = true)

        } catch (e: IOException) {
            Resource.Error("Could not save goal to the database.Issue : ${e.message}")
        } catch (e: SQLiteException) {
            Resource.Error("Could not save goals to the database.Issue : ${e.message}")
        }
    }

    override fun getActiveGoals(): Flow<List<Goal>> {
        return goalsLocalDataSource.getCachedGoals()
            .map { goalEntities ->
                goalEntities.filter {
                    it.isActive
                }
            }
            .map { goalsList ->
                goalsList.map {
                    it.toDomain()
                }
            }.catch { t ->
                Timber.tag(TAG).e("getAllGoals : $t")
                Timber.tag(TAG).e(t)
            }
    }

    override suspend fun deleteGoal(id: String): Resource<Boolean> {
        return try {
            goalsLocalDataSource.deleteCachedGoal(id)
            Resource.Success(data = true)
        } catch (e: IOException) {
            Timber.tag(TAG).e("deleteGoal : $e")
            Resource.Error("Could not delete goal in the database.Issue : ${e.message}")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("deleteGoal : $e")
            Resource.Error("Could not delete goal in the database.Issue : ${e.message}")
        }
    }

    override suspend fun getAGoal(id: String): Resource<Goal> {
        return try {
            Resource.Success(
                data = goalsLocalDataSource.getCachedGoalFromDatabase(id).toDomain()
            )
        } catch (e: IOException) {
            Timber.tag(TAG).e("getAGoal : $e")
            Resource.Error("Could not fetch goal in the database.Issue : ${e.message}")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("getAGoal : $e")
            Resource.Error("Could not fetch goal in the database.Issue : ${e.message}")
        }
    }

    override suspend fun updateBooksRead(goalId: String, bookCount: Int) {
        try {
            val goalUpdate = GoalUpdate(
                id = goalId ,
                booksRead = bookCount
            )
            goalsLocalDataSource.updateBooksReadCount(goalUpdate)
        } catch (e: IOException) {
            Timber.tag(TAG).e("updateBooksRead : ${e.localizedMessage}")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("updateBooksRead : ${e.localizedMessage}")
        }
    }


}