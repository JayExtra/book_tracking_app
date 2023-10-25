package com.dev.james.data.datasources.logs

import com.dev.james.booktracker.core_database.room.dao.LogsDao
import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity
import com.dev.james.domain.datasources.home.LogsLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogsLocalDataSourceImpl @Inject constructor(
    private val logsDao: LogsDao
) : LogsLocalDataSource {
    override suspend fun addBookLogToDatabase(bookLogsEntity: BookLogsEntity) {
        logsDao.addBookLog(bookLogsEntity)
    }

    override fun getBookLogs(bookId: String): Flow<List<BookLogsEntity>> =
        logsDao.getBookLogs(bookId = bookId)

    override suspend fun getBookLog(id: String): BookLogsEntity {
        return logsDao.getABookLog(id)
    }

    override suspend fun deleteBookLog(id: String) {
        logsDao.deleteBookLog(id)
    }

    override suspend fun addGoalLogToDatabase(goalLogsEntity: GoalLogsEntity) {
        logsDao.addGoalLog(goalLogsEntity)
    }

    override fun getGoalLogs(goalId: String): Flow<List<GoalLogsEntity>> =
        logsDao.getGoalLogs(goalId)

    override suspend fun getGoalLog(id: String): GoalLogsEntity {
        return logsDao.getAGoalLog(id)
    }

    override suspend fun deleteGoalLog(id: String) {
        logsDao.deleteGoalLog(id)
    }
}