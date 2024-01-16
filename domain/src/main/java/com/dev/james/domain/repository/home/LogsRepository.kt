package com.dev.james.domain.repository.home

import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface LogsRepository {

    suspend fun addBookLog(bookLog: BookLog) : Resource<Boolean>
    fun getBookLogs(bookId : String) : Flow<List<BookLog>>

    suspend fun getBookLog(id : String) : Resource<BookLog>

    suspend fun deleteBookLog(id : String) : Resource<Boolean>


    suspend fun addGoalLog(goalLog : GoalLog) : Resource<Boolean>
    fun getGoalLogs(goalId : String) : Flow<List<GoalLog>>

    suspend fun getGoalLog(id : String) : Resource<GoalLog>

    suspend fun deleteGoalLog(id : String) : Resource<Boolean>

}