package com.dev.james.data.repositories.logs

import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.domain.datasources.home.LogsLocalDataSource
import com.dev.james.domain.repository.home.LogsRepository
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
            }.catch {
                emit(emptyList<BookGoalLog>())
            }

}