package com.dev.james.domain.datasources.home

import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity

interface LogsLocalDataSource {

    suspend fun addBookLogToDatabase(bookLogsEntity: BookLogsEntity)
    suspend fun getWeeklyBookLogs(bookId : String, startDate :  String, endDate : String) : List<BookLogsEntity>

    suspend fun getAllBookLogs(bookId: String) : List<BookLogsEntity>

    suspend fun getBookLog(id : String) : BookLogsEntity

    suspend fun deleteBookLog(id : String)

    suspend fun addGoalLogToDatabase(goalLogsEntity: GoalLogsEntity)
    suspend fun getWeeklyGoalLogs(parentLogId : String, startDate :  String, endDate : String) : List<GoalLogsEntity>

    suspend fun getAllGoalLogs(parentGoalId: String) : List<GoalLogsEntity>

    suspend fun getGoalLog(id: String) : GoalLogsEntity

    suspend fun deleteGoalLog(id : String)

    suspend fun fetchLatestBookLog(bookId: String) : List<BookLogsEntity>

    suspend fun fetchLatestGoalLog(parentGoalId: String) : List<GoalLogsEntity>

}