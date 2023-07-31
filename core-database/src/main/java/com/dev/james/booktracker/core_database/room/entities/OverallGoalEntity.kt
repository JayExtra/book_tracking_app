package com.dev.james.booktracker.core_database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "overall_goals_table")
data class OverallGoalEntity(
    @ColumnInfo("id")
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
    val alertTime : String
)