package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dev.james.booktracker.core.entities.BookGoalLogsEntity
import com.dev.james.booktracker.core.entities.OverallGoalLogsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogsDao {
    @Query("SELECT * FROM book_goal_logs WHERE book_id =:id")
    fun getBookGoaLogs(id : String) : Flow<List<BookGoalLogsEntity>>

    @Query("SELECT * FROM daily_overall_goal_logs WHERE parent_goal_id =:id")
    fun getOverallGoalLogs(id : String) : Flow<List<OverallGoalLogsEntity>>
}