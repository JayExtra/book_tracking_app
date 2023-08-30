package com.dev.james.booktracker.home.domain.datasources

import com.dev.james.booktracker.core_database.room.entities.BookGoalLogsEntity
import com.dev.james.booktracker.core_database.room.entities.OverallGoalLogsEntity
import kotlinx.coroutines.flow.Flow

interface LogsLocalDataSource {

    fun getBookGoalLogs(id : String) : Flow<List<BookGoalLogsEntity>>

    fun getOverallGoalLogs(id : String) : Flow<List<OverallGoalLogsEntity>>

}