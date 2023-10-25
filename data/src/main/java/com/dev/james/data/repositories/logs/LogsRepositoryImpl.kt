package com.dev.james.data.repositories.logs

import android.database.sqlite.SQLiteException
import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.common_models.mappers.toEntity
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.datasources.home.LogsLocalDataSource
import com.dev.james.domain.repository.home.LogsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import timber.log.Timber
import javax.inject.Inject

class LogsRepositoryImpl @Inject constructor(
    private val logsLocalDataSource: LogsLocalDataSource
) : LogsRepository {
    companion object {
        const val TAG = "LogsRepositoryImpl"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addBookLog(bookLog: BookLog): Resource<Boolean> {
        return try {
            logsLocalDataSource.addBookLogToDatabase(bookLog.toEntity())
            Resource.Success(data = true)
        }catch (e : IOException){
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(data = false , message = "Could not log your current progress! Reason: $e ")
        }catch (e : SQLiteException){
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(data = false , message = "Could not log your current progress! Reason: $e ")
        }
    }

    override fun getBookLogs(bookId: String): Flow<List<BookLog>> =
        logsLocalDataSource.getBookLogs(bookId)
            .map { bookGoalLogsEntityList ->
                bookGoalLogsEntityList.map { bookGoalLogsEntity ->
                    bookGoalLogsEntity.toDomain()
                }
            }.catch { t->
                Timber.tag(TAG).e("getBookLogs : $t")
                emit(emptyList<BookLog>())
            }

    override suspend fun getBookLog(id: String): Resource<BookLog> {
        return try {
            Resource.Success(data = logsLocalDataSource.getBookLog(id).toDomain())
        }catch (e : IOException){
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(message = "Could not get this log! Reason: $e ")
        }catch (e : SQLiteException){
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(message = "Could not get this log! Reason: $e ")
        }
    }

    override suspend fun deleteBookLog(id: String): Resource<Boolean> {
        return try {
            logsLocalDataSource.deleteBookLog(id)
            Resource.Success(data = true)
        }catch (e : IOException){
            Timber.tag(TAG).e("deleteBookLog $e")
            Resource.Error(data = false , message = "Could not delete this log! Reason: $e ")
        }catch (e : SQLiteException){
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(data = false , message = "Could not delete this log! Reason: $e ")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addGoalLog(goalLog: GoalLog): Resource<Boolean> {
        return try {
            logsLocalDataSource.addGoalLogToDatabase(goalLog.toEntity())
            Resource.Success(data = true)
        }catch (e : IOException){
            Timber.tag(TAG).e("addGoalLog $e")
            Resource.Error(data = false , message = "Could not log your current progress! Reason: $e ")
        }catch (e : SQLiteException){
            Timber.tag(TAG).e("addGoalLog $e")
            Resource.Error(data = false , message = "Could not log your current progress! Reason: $e ")
        }
    }

    override fun getGoalLogs(goalId: String): Flow<List<GoalLog>> =
        logsLocalDataSource.getGoalLogs(goalId = goalId)
            .map { goalLogsList ->
                goalLogsList.map { goalLogsEntity ->
                    goalLogsEntity.toDomain()
                }
            }.catch { t->
                Timber.tag(TAG).e("getGoalLogs: $t")
                emit(emptyList<GoalLog>())
            }
    override suspend fun getGoalLog(id: String): Resource<GoalLog> {
        return try {
            Resource.Success(data = logsLocalDataSource.getGoalLog(id).toDomain())
        }catch (e : IOException){
            Timber.tag(TAG).e("getGoalLog $e")
            Resource.Error(message = "Could not get this log! Reason: $e ")
        }catch (e : SQLiteException){
            Timber.tag(TAG).e("getGoalLog $e")
            Resource.Error(message = "Could not get this log! Reason: $e ")
        }
    }

    override suspend fun deleteGoalLog(id: String): Resource<Boolean> {
        return try {
            logsLocalDataSource.deleteGoalLog(id)
            Resource.Success(data = true)
        }catch (e : IOException){
            Timber.tag(TAG).e("deleteGoalLog $e")
            Resource.Error(data = false , message = "Could not delete this log! Reason: $e ")
        }catch (e : SQLiteException){
            Timber.tag(TAG).e("deleteGoalLog $e")
            Resource.Error(data = false , message = "Could not delete this log! Reason: $e ")
        }
    }

}