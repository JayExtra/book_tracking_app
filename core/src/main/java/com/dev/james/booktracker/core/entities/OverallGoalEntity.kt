package com.dev.james.booktracker.core.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "overall_goals_table")
data class OverallGoalEntity(
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = false)
    val goalId : String ,
    @ColumnInfo("goal_info")
    val goalInfo : String ,
    @ColumnInfo("goal_time")
    val goalTime : Long ,
    @ColumnInfo("goal_period")
    val goalPeriod : String ,
    @ColumnInfo("specific_days")
    val specificDays : List<String> ,
    @ColumnInfo("should_show_alert")
    val shouldShowAlert : Boolean ,
    @ColumnInfo("alert_note")
    val alertNote : String ,
    @ColumnInfo("alert_time")
    val alertTime : String ,
    @ColumnInfo("is_goal_active" , defaultValue = "true")
    val isActive : Boolean = true
)