package com.dev.james.booktracker.core_database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.Date

@Entity("daily_overall_goal_logs")
data class OverallGoalLogsEntity(
    @ColumnInfo("parent_goal_id")
    val parentGoalId : String ,
    @ColumnInfo("log_id")
    val logId : String ,
    @ColumnInfo("start_time")
    val startTime : Date ,
    @ColumnInfo("end_time")
    val endTime : Date ,
    @ColumnInfo("duration_taken")
    val duration : Long
)
