package com.dev.james.booktracker.core_database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Date

@Entity("book_goal_logs")
data class BookGoalLogsEntity(
    @ColumnInfo("book_id")
    val bookId : String,
    @ColumnInfo("log_id")
    val logId : String,
    @ColumnInfo("started_time")
    val startedTime : Date,
    @ColumnInfo("end_time")
    val endTime : Date ,
    @ColumnInfo("period_taken")
    val period : Long
)
