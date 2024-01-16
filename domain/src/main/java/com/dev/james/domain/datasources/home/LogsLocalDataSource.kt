package com.dev.james.domain.datasources.home

import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity
import kotlinx.coroutines.flow.Flow

interface LogsLocalDataSource {

    suspend fun addBookLogToDatabase(bookLogsEntity: BookLogsEntity)
    fun getBookLogs(bookId : String) : Flow<List<BookLogsEntity>>

    suspend fun getBookLog(id : String) : BookLogsEntity

    suspend fun deleteBookLog(id : String)

    suspend fun addGoalLogToDatabase(goalLogsEntity: GoalLogsEntity)
    fun getGoalLogs(goalId : String) : Flow<List<GoalLogsEntity>>

    suspend fun getGoalLog(id: String) : GoalLogsEntity

    suspend fun deleteGoalLog(id : String)



}