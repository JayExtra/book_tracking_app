package com.dev.james.booktracker.core.common_models

import java.time.LocalDate

data class BookLog(
    val bookId : String = "",
    val logId : String = "",
    val startedTime : LocalDate? = null,
    val endTime : LocalDate? = null,
    val period : Long = 0L,
    val pagesRead : Int = 0 ,
    val currentChapterTitle : String = "" ,
    val currentChapter : Int = 0
)
