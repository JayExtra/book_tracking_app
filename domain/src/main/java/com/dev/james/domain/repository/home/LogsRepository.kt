package com.dev.james.domain.repository.home

import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface LogsRepository {

    suspend fun addBookLog(bookLog: BookLog) : Resource<Boolean>
    suspend fun getBookLogs(bookId : String , mondayDate : String , sundayDate : String) : List<BookLog>

    suspend fun getBookLog(id : String) : Resource<BookLog>

    suspend fun deleteBookLog(id : String) : Resource<Boolean>

    suspend fun addGoalLog(goalLog : GoalLog) : Resource<Boolean>
    suspend fun getGoalLogs(parentLogId : String , mondayDate : String , sundayDate : String) : List<GoalLog>

    suspend fun getGoalLog(id : String) : Resource<GoalLog>

    suspend fun deleteGoalLog(id : String) : Resource<Boolean>

    suspend fun getRecentGoalLog() : GoalLog

    suspend fun getRecentBookLog() : BookLog

}