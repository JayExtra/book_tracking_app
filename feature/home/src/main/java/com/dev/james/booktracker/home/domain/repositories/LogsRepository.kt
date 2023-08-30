package com.dev.james.booktracker.home.domain.repositories

import com.dev.james.booktracker.core.common_models.BookGoalLog
import kotlinx.coroutines.flow.Flow

interface LogsRepository {

    fun getBookGoalLogs(id : String) : Flow<List<BookGoalLog>>

}