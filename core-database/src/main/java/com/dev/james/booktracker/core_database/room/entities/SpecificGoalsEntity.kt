package com.dev.james.booktracker.core_database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("specific_goals_table")
data class SpecificGoalsEntity(
    @ColumnInfo("goal_id")
    val goalId : String ,
    @ColumnInfo("book_count_goal")
    val bookCountGoal : Int ,
    @ColumnInfo("books_read_count")
    val booksReadCount : Int
)
