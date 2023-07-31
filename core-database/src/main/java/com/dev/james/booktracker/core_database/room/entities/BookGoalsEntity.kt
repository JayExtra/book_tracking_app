package com.dev.james.booktracker.core_database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity("book_goal_entity")
data class BookGoalsEntity(
    @ColumnInfo("book_id")
    val bookId : String ,
    @ColumnInfo("is_chapter_goal")
    val isChapterGoal : Boolean ,
    @ColumnInfo("goal_information")
    val goalInfo : String,
    @ColumnInfo("is_time_goal")
    val isTimeGoal: Boolean ,
    //goals set will be either the chapter count or the time to be spent
    @ColumnInfo("goal_set")
    val goalSet : String ,
    @ColumnInfo("goal_period")
    val goalPeriod : String ,
    @ColumnInfo("specific_days_set")
    val specificDays : List<String>
)
