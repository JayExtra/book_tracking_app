package com.dev.james.booktracker.core.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity("daily_goal_logs")
data class GoalLogsEntity(
    @ColumnInfo("parent_goal_id")
    val parentGoalId : String,
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = false)
    val id : String,
    @ColumnInfo("start_time")
    val startTime : LocalDate,
    @ColumnInfo("duration_taken")
    val duration : Long
)
