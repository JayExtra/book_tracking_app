package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity
import com.dev.james.booktracker.core.entities.updates.BookLogUpdate
import com.dev.james.booktracker.core.entities.updates.GoalLogUpdate

@Dao
interface LogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookLog(bookLogsEntity: BookLogsEntity)

    @Query("SELECT * FROM book_logs WHERE book_id = :bookId ORDER BY started_time DESC")
    suspend fun getAllBookLogs(bookId: String) : List<BookLogsEntity>
    @Query("SELECT * FROM book_logs WHERE book_id = :bookId AND started_time >= :startDate AND started_time <= :endDate")
    suspend fun getWeeklyBookLogs(bookId : String, startDate: String, endDate: String) : List<BookLogsEntity>

    @Query("DELETE FROM book_logs WHERE log_id = :id")
    suspend fun deleteBookLog(id : String)

    @Query("SELECT * FROM book_logs WHERE log_id = :id ")
    suspend fun getABookLog(id : String) : BookLogsEntity

    @Query("SELECT * FROM book_logs WHERE book_id = :bookId ORDER BY started_time DESC LIMIT 1 ")
    suspend fun getRecentBookLog(bookId : String) : List<BookLogsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGoalLog(goalLogsEntity: GoalLogsEntity)
    @Query("SELECT * FROM daily_goal_logs WHERE parent_goal_id = :parentLogId AND start_time >= :startDate AND start_time <= :endDate ")
    suspend fun getWeeklyGoalLogs(parentLogId : String, startDate: String, endDate: String) : List<GoalLogsEntity>

    @Query("SELECT * FROM daily_goal_logs WHERE parent_goal_id = :parentGoalId ORDER BY start_time DESC")
    suspend fun getAllGoalLogs(parentGoalId: String) : List<GoalLogsEntity>

    @Query("DELETE FROM daily_goal_logs WHERE id = :id")
    suspend fun deleteGoalLog(id : String)

    @Query("DELETE FROM daily_goal_logs WHERE parent_goal_id =:parentGoalId")
    suspend fun deleteAllGoalLogsByParentId(parentGoalId: String)

    @Query("SELECT * FROM daily_goal_logs WHERE id = :id")
    suspend fun getAGoalLog(id : String) : GoalLogsEntity

    @Query("SELECT * FROM daily_goal_logs WHERE parent_goal_id =:parentGoalId ORDER BY start_time DESC LIMIT 1")
    suspend fun getRecentGoalLog(parentGoalId: String) : List<GoalLogsEntity>


    @Update(BookLogsEntity::class)
    suspend fun updateBookLog(bookLogUpdate: BookLogUpdate)

    @Update(GoalLogsEntity::class)
    suspend fun updateGoalLog(goalLogUpdate: GoalLogUpdate)

}