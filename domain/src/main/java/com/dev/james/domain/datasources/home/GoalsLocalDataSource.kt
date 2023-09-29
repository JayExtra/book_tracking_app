package com.dev.james.domain.datasources.home

import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.entities.BookGoalLogsEntity
import com.dev.james.booktracker.core.entities.BookGoalsEntity
import com.dev.james.booktracker.core.entities.OverallGoalEntity
import com.dev.james.booktracker.core.entities.SpecificGoalsEntity

interface GoalsLocalDataSource {

    suspend fun addOverallGoalToDatabase(overallGoalEntity: OverallGoalEntity)
    suspend fun addSpecificGoalToDatabase(specificGoalsEntity: SpecificGoalsEntity)
    suspend fun addBookGoalToDatabase(bookGoalsEntity: BookGoalsEntity)

    suspend fun getAllBookGoals() : List<BookGoalsEntity>

}