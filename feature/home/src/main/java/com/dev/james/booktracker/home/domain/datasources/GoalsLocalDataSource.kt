package com.dev.james.booktracker.home.domain.datasources

import com.dev.james.booktracker.core_database.room.entities.BookGoalLogsEntity
import com.dev.james.booktracker.core_database.room.entities.BookGoalsEntity
import com.dev.james.booktracker.core_database.room.entities.OverallGoalEntity
import com.dev.james.booktracker.core_database.room.entities.SpecificGoalsEntity
import kotlinx.coroutines.flow.Flow

interface GoalsLocalDataSource {

    suspend fun addOverallGoalToDatabase(overallGoalEntity: OverallGoalEntity)
    suspend fun addSpecificGoalToDatabase(specificGoalsEntity: SpecificGoalsEntity)
    suspend fun addBookGoalToDatabase(bookGoalsEntity: BookGoalsEntity)

    suspend fun getAllBookGoals() : List<BookGoalsEntity>

}