package com.dev.james.data.datasources.logs

import com.dev.james.booktracker.core_database.room.dao.LogsDao
import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity
import com.dev.james.domain.datasources.home.LogsLocalDataSource
import javax.inject.Inject

class LogsLocalDataSourceImpl @Inject constructor(
    private val logsDao: LogsDao
) : LogsLocalDataSource {
    override suspend fun addBookLogToDatabase(bookLogsEntity: BookLogsEntity) {
        logsDao.addBookLog(bookLogsEntity)
    }

    override suspend fun getWeeklyBookLogs(
        bookId: String,
        startDate: String,
        endDate: String
    ): List<BookLogsEntity> =
        logsDao.getWeeklyBookLogs(bookId = bookId , startDate = startDate , endDate = endDate)

    override suspend fun getAllBookLogs(bookId: String): List<BookLogsEntity> {
        return logsDao.getAllBookLogs(bookId)
    }

    override suspend fun getBookLog(id: String): BookLogsEntity {
        return logsDao.getABookLog(id = id )
    }

    override suspend fun deleteBookLog(id: String) {
        logsDao.deleteBookLog(id)
    }

    override suspend fun addGoalLogToDatabase(goalLogsEntity: GoalLogsEntity) {
        logsDao.addGoalLog(goalLogsEntity)
    }

    override suspend fun getWeeklyGoalLogs(parentLogId : String, startDate :  String, endDate : String): List<GoalLogsEntity> =
        logsDao.getWeeklyGoalLogs(parentLogId = parentLogId, startDate = startDate , endDate = endDate)

    override suspend fun getAllGoalLogs(parentGoalId: String): List<GoalLogsEntity> {
        return logsDao.getAllGoalLogs(parentGoalId)
    }

    override suspend fun getGoalLog(id: String): GoalLogsEntity {
        return logsDao.getAGoalLog(id)
    }

    override suspend fun deleteGoalLog(id: String) {
        logsDao.deleteGoalLog(id)
    }

    override suspend fun fetchLatestBookLog(bookId: String): List<BookLogsEntity> {
        return logsDao.getRecentBookLog(bookId)
    }

    override suspend fun fetchLatestGoalLog(parentGoalId: String): List<GoalLogsEntity> {
        return logsDao.getRecentGoalLog(parentGoalId)
    }
}