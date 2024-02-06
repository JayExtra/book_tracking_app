package com.dev.james.domain.datasources.home

import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity
import com.dev.james.booktracker.core.entities.updates.BookLogUpdate
import com.dev.james.booktracker.core.entities.updates.GoalLogUpdate

interface LogsLocalDataSource {

    suspend fun addBookLogToDatabase(bookLogsEntity: BookLogsEntity)

    suspend fun updateBookLog(bookLogUpdate: BookLogUpdate)

    suspend fun getWeeklyBookLogs(bookId : String, startDate :  String, endDate : String) : List<BookLogsEntity>

    suspend fun getAllBookLogs(bookId: String) : List<BookLogsEntity>

    suspend fun getBookLog(id : String) : BookLogsEntity

    suspend fun deleteBookLog(id : String)

    suspend fun addGoalLogToDatabase(goalLogsEntity: GoalLogsEntity)

    suspend fun updateGoalLog(goalLogUpdate: GoalLogUpdate)
    suspend fun getWeeklyGoalLogs(parentLogId : String, startDate :  String, endDate : String) : List<GoalLogsEntity>

    suspend fun getAllGoalLogs(parentGoalId: String) : List<GoalLogsEntity>

    suspend fun getGoalLog(id: String) : GoalLogsEntity

    suspend fun deleteGoalLog(id : String)

    suspend fun fetchLatestBookLog(bookId: String) : List<BookLogsEntity>

    suspend fun fetchLatestGoalLog(parentGoalId: String) : List<GoalLogsEntity>





}