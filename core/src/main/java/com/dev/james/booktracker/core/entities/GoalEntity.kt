package com.dev.james.booktracker.core.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals_table")
data class GoalEntity(
    @ColumnInfo("id")
    @PrimaryKey(autoGenerate = false)
    val id : String,
    @ColumnInfo("information")
    val information : String,
    @ColumnInfo("time")
    val time : Long,
    @ColumnInfo("period")
    val period : String,
    @ColumnInfo("selected_days")
    val selectedDays : List<String>,
    @ColumnInfo("should_show_alert")
    val shouldShowAlert : Boolean,
    @ColumnInfo("alert_note")
    val alertNote : String,
    @ColumnInfo("alert_time")
    val alertTime : String,
    @ColumnInfo("books_to_read")
    val booksToRead : Int,
    @ColumnInfo("books_read")
    val booksRead : Int ,
    @ColumnInfo("is_goal_active" , defaultValue = "true")
    val isActive : Boolean = true
)