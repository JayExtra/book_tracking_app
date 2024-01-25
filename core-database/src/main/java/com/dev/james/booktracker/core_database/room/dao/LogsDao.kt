package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookLog(bookLogsEntity: BookLogsEntity)
    @Query("SELECT * FROM book_logs WHERE book_id = :bookId AND started_time >= :startDate AND started_time <= :endDate")
    suspend fun getBookLogs(bookId : String , startDate: String , endDate: String) : List<BookLogsEntity>

    @Query("DELETE FROM book_logs WHERE log_id = :id")
    suspend fun deleteBookLog(id : String)

    @Query("SELECT * FROM book_logs WHERE log_id = :id ")
    suspend fun getABookLog(id : String) : BookLogsEntity

    @Query("SELECT * FROM book_logs ORDER BY started_time DESC LIMIT 1 ")
    suspend fun getRecentBookLog() : BookLogsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGoalLog(goalLogsEntity: GoalLogsEntity)
    @Query("SELECT * FROM daily_goal_logs WHERE parent_goal_id = :parentLogId AND start_time >= :startDate AND start_time <= :endDate ")
    suspend fun getGoalLogs( parentLogId : String , startDate: String , endDate: String) : List<GoalLogsEntity>

    @Query("DELETE FROM daily_goal_logs WHERE id = :id")
    suspend fun deleteGoalLog(id : String)

    @Query("SELECT * FROM daily_goal_logs WHERE id = :id")
    suspend fun getAGoalLog(id : String) : GoalLogsEntity

    @Query("SELECT * FROM daily_goal_logs ORDER BY start_time DESC LIMIT 1")
    suspend fun getRecentGoalLog() : GoalLogsEntity




}