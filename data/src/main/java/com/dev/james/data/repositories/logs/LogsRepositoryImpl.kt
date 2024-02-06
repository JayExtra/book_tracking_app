package com.dev.james.data.repositories.logs

import android.database.sqlite.SQLiteException
import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.common_models.mappers.toEntity
import com.dev.james.booktracker.core.entities.updates.BookLogUpdate
import com.dev.james.booktracker.core.entities.updates.GoalLogUpdate
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.domain.datasources.home.LogsLocalDataSource
import com.dev.james.domain.repository.home.LogsRepository
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

        } catch (e: IOException) {
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(
                data = false,
                message = "Could not log your current progress! Reason: $e "
            )
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(
                data = false,
                message = "Could not log your current progress! Reason: $e "
            )
        }
    }

    override suspend fun updateBookLog(bookLogUpdate: BookLogUpdate) {
        try {
            logsLocalDataSource.updateBookLog(bookLogUpdate)
        } catch (e: IOException) {
            Timber.tag(TAG).e("updateBookLog ${e.localizedMessage}")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("updateBookLog ${e.localizedMessage}")
        }
    }

    override suspend fun updateGoalLog(goalLogUpdate: GoalLogUpdate) {
        try {
            logsLocalDataSource.updateGoalLog(goalLogUpdate)
        } catch (e: IOException) {
            Timber.tag(TAG).e("updateGoalLog ${e.localizedMessage}")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("updateGoalLog ${e.localizedMessage}")
        }
    }

    override suspend fun getWeeklyBookLogs(
        bookId: String,
        startDate: String,
        endDate: String
    ): List<BookLog> {
        return try{
            logsLocalDataSource.getWeeklyBookLogs(bookId = bookId , startDate = startDate , endDate = endDate)
                .map { bookLogsEntity ->
                    bookLogsEntity.toDomain()
                }
        }catch (e : Exception){
            if(e is SQLiteException){
                Timber.tag(TAG).e("SQL Lite exception => ${e.localizedMessage}")
            }else{
                Timber.tag(TAG).e("Exception => ${e.localizedMessage}")
            }
            listOf(BookLog())
        }
    }


    override suspend fun getAllBookLogs(bookId: String): List<BookLog> {
        return try {
            logsLocalDataSource.getAllBookLogs(bookId).map {
                it.toDomain()
            }
        }catch (e : Exception){
            //specify which exeption though
            if(e is SQLiteException){
                Timber.tag(TAG).e("SQL Lite exception => ${e.localizedMessage}")
            }else{
                Timber.tag(TAG).e("Exception => ${e.localizedMessage}")
            }
            listOf(BookLog())
        }
    }

    override suspend fun getBookLog(
        id: String
    ): Resource<BookLog> {
        return try {
            Resource.Success(
                data = logsLocalDataSource.getBookLog(
                    id = id
                ).toDomain()
            )
        } catch (e: IOException) {
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(message = "Could not get this log! Reason: $e ")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(message = "Could not get this log! Reason: $e ")
        }
    }

    override suspend fun deleteBookLog(id: String): Resource<Boolean> {
        return try {
            logsLocalDataSource.deleteBookLog(id)
            Resource.Success(data = true)
        } catch (e: IOException) {
            Timber.tag(TAG).e("deleteBookLog $e")
            Resource.Error(data = false, message = "Could not delete this log! Reason: $e ")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("addBookLog $e")
            Resource.Error(data = false, message = "Could not delete this log! Reason: $e ")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addGoalLog(goalLog: GoalLog): Resource<Boolean> {
        return try {
            logsLocalDataSource.addGoalLogToDatabase(goalLog.toEntity())
            Resource.Success(data = true)
        } catch (e: IOException) {
            Timber.tag(TAG).e("addGoalLog $e")
            Resource.Error(
                data = false,
                message = "Could not log your current progress! Reason: $e "
            )
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("addGoalLog $e")
            Resource.Error(
                data = false,
                message = "Could not log your current progress! Reason: $e "
            )
        }
    }

    override suspend fun getWeeklyGoalLogs(parentLogId:String, startDate: String, endDate: String): List<GoalLog> {
        return try {
            logsLocalDataSource.getWeeklyGoalLogs( parentLogId = parentLogId, startDate = startDate , endDate = endDate)
                .map { goalLogsEntity ->
                    goalLogsEntity.toDomain()
                }
        }catch ( e : Exception){
            if(e is SQLiteException){
                Timber.tag(TAG).e("SQL Lite exception => ${e.localizedMessage}")
            }else{
                Timber.tag(TAG).e("Exception => ${e.localizedMessage}")
            }
            listOf(GoalLog())
        }
    }

    override suspend fun getAllGoalLogs(parentGoalId: String): List<GoalLog> {
        return try {
            logsLocalDataSource.getAllGoalLogs(parentGoalId).map {
                it.toDomain()
            }
        }catch ( e: Exception){
            if(e is SQLiteException){
                Timber.tag(TAG).e("SQL Lite exception => ${e.localizedMessage}")
            }else{
                Timber.tag(TAG).e("Exception => ${e.localizedMessage}")
            }
            listOf(GoalLog())
        }
    }

    override suspend fun getGoalLog(id: String): Resource<GoalLog> {
        return try {
            Resource.Success(data = logsLocalDataSource.getGoalLog(id).toDomain())
        } catch (e: IOException) {
            Timber.tag(TAG).e("getGoalLog $e")
            Resource.Error(message = "Could not get this log! Reason: $e ")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("getGoalLog $e")
            Resource.Error(message = "Could not get this log! Reason: $e ")
        }
    }

    override suspend fun deleteGoalLog(id: String): Resource<Boolean> {
        return try {
            logsLocalDataSource.deleteGoalLog(id)
            Resource.Success(data = true)
        } catch (e: IOException) {
            Timber.tag(TAG).e("deleteGoalLog $e")
            Resource.Error(data = false, message = "Could not delete this log! Reason: $e ")
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("deleteGoalLog $e")
            Resource.Error(data = false, message = "Could not delete this log! Reason: $e ")
        }
    }


    override suspend fun getRecentGoalLog(parentGoalId: String): GoalLog {
        return try {
            val latestGoalLog = logsLocalDataSource.fetchLatestGoalLog(parentGoalId)
            return if(latestGoalLog.isEmpty()){
                 GoalLog()
            }else{
                latestGoalLog.first().toDomain()
            }
        }catch (e : Exception){
            Timber.tag(TAG).e("DB error $e : ${e.localizedMessage}")
            GoalLog()
        }
    }

    override suspend fun getRecentBookLog(bookId: String): BookLog {
        return try {
            val recentBookLogList = logsLocalDataSource.fetchLatestBookLog(bookId)
            return if(recentBookLogList.isEmpty()){
                BookLog()
            }else{
                recentBookLogList.first().toDomain()
            }
        }catch (e : Exception){
            Timber.tag(TAG).e("DB error $e : ${e.localizedMessage}")
            BookLog()
        }
    }

}