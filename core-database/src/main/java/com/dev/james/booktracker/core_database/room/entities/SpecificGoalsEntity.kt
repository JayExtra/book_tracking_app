package com.dev.james.booktracker.core_database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("specific_goals_table")
data class SpecificGoalsEntity(
    @ColumnInfo("goal_id")
    @PrimaryKey(autoGenerate = false)
    val goalId : String ,
    @ColumnInfo("book_count_goal")
    val bookCountGoal : Int ,
    @ColumnInfo("books_read_count")
    val booksReadCount : Int ,
    @ColumnInfo("is_goal_active" , defaultValue = "true")
    val isActive : Boolean = true
)
