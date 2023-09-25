package com.dev.james.booktracker.core.common_models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.time.LocalDate

data class BookGoalLog(
    val bookId : String = "",
    val logId : String = "",
    val startedTime : LocalDate? = null,
    val endTime : LocalDate? = null,
    val period : Long = 0L,
    val pagesRead : Int = 0 ,
    val currentChapterTitle : String = "" ,
    val currentChapter : Int = 0
)
