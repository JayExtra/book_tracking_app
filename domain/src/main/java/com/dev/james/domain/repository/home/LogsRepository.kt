package com.dev.james.domain.repository.home

import com.dev.james.booktracker.core.common_models.BookGoalLog
import kotlinx.coroutines.flow.Flow

interface LogsRepository {

    fun getBookGoalLogs(id : String) : Flow<List<BookGoalLog>>

}