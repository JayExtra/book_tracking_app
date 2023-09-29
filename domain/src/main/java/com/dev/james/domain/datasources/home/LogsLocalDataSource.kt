package com.dev.james.domain.datasources.home

import com.dev.james.booktracker.core.entities.BookGoalLogsEntity
import com.dev.james.booktracker.core.entities.OverallGoalLogsEntity
import kotlinx.coroutines.flow.Flow

interface LogsLocalDataSource {

    fun getBookGoalLogs(id : String) : Flow<List<BookGoalLogsEntity>>

    fun getOverallGoalLogs(id : String) : Flow<List<OverallGoalLogsEntity>>



}