package com.dev.james.domain.datasources.home

import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity

interface LogsLocalDataSource {

    suspend fun addBookLogToDatabase(bookLogsEntity: BookLogsEntity)
    suspend fun getBookLogs(bookId : String , startDate :  String , endDate : String) : List<BookLogsEntity>

    suspend fun getBookLog(id : String) : BookLogsEntity

    suspend fun deleteBookLog(id : String)

    suspend fun addGoalLogToDatabase(goalLogsEntity: GoalLogsEntity)
    suspend fun getAllGoalLogs(parentLogId : String , startDate :  String, endDate : String) : List<GoalLogsEntity>

    suspend fun getGoalLog(id: String) : GoalLogsEntity

    suspend fun deleteGoalLog(id : String)



}