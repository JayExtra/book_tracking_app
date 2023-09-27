package com.dev.james.data.datasources.logs

import com.dev.james.booktracker.core_database.room.dao.LogsDao
import com.dev.james.booktracker.core.entities.BookGoalLogsEntity
import com.dev.james.booktracker.core.entities.OverallGoalLogsEntity
import com.dev.james.domain.datasources.home.LogsLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogsLocalDataSourceImpl @Inject constructor(
    private val logsDao: LogsDao
) : LogsLocalDataSource {
    override fun getBookGoalLogs(id: String): Flow<List<BookGoalLogsEntity>> =
        logsDao.getBookGoaLogs(id = id)

    override fun getOverallGoalLogs(id: String): Flow<List<OverallGoalLogsEntity>> =
        logsDao.getOverallGoalLogs(id)
}