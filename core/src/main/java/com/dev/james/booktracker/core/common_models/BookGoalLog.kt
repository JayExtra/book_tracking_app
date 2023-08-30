package com.dev.james.booktracker.core.common_models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.time.LocalDate

data class BookGoalLog(
    val bookId : String,
    val logId : String,
    val startedTime : LocalDate,
    val endTime : LocalDate,
    val period : Long,
    val pagesRead : Int ,
    val currentChapterTitle : String ,
    val currentChapter : Int
)
