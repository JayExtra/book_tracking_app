package com.dev.james.booktracker.home.data.repository

import android.database.sqlite.SQLiteException
import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.OverallGoal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.common_models.mappers.mapToEntityObject
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.home.domain.datasources.GoalsLocalDataSource
import com.dev.james.booktracker.home.domain.repositories.GoalsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class GoalsRepositoryImpl @Inject constructor(
    private val goalsLocalDataSource: GoalsLocalDataSource ,
    private val defaultDispatcher : CoroutineDispatcher = Dispatchers.IO
) : GoalsRepository {
    override suspend fun addOverallGoal(overallGoal: OverallGoal): Resource<Boolean> {
        return try {
            goalsLocalDataSource.addOverallGoalToDatabase(overallGoal.mapToEntityObject())
            Resource.Success(true)
        }catch (e : IOException){
            Resource.Error(e.localizedMessage as String)
        }catch (e : SQLiteException){
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun addSpecificGoal(specificGoal: SpecificGoal): Resource<Boolean> {
       return try {
           goalsLocalDataSource.addSpecificGoalToDatabase(specificGoal.mapToEntityObject())
           Resource.Success(true)
       }catch (e : IOException){
           Resource.Error(e.localizedMessage as String)
       }catch (e : SQLiteException){
           Resource.Error(e.localizedMessage as String)
       }
    }

    override suspend fun addBookGoal(bookGoal: BookGoal): Resource<Boolean> {
        return try {
            goalsLocalDataSource.addBookGoalToDatabase(bookGoal.mapToEntityObject())
            Resource.Success(true)
        }catch (e : IOException){
            Resource.Error(e.localizedMessage as String)
        }catch (e : SQLiteException){
            Resource.Error(e.localizedMessage as String)
        }
    }
}