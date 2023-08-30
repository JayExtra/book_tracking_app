package com.dev.james.booktracker.home.data.repository

import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.home.domain.datasources.LogsLocalDataSource
import com.dev.james.booktracker.home.domain.repositories.LogsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogsRepositoryImpl @Inject constructor(
    private val logsLocalDataSource: LogsLocalDataSource
) : LogsRepository {
    override fun getBookGoalLogs(id: String): Flow<List<BookGoalLog>> =
        logsLocalDataSource.getBookGoalLogs(id)
            .map { bookGoalLogsEntityList ->
                bookGoalLogsEntityList.map { bookGoalLogsEntity ->
                    bookGoalLogsEntity.toDomain()
                }
            }.catch { throwable ->
                emit(emptyList<BookGoalLog>())
            }

}