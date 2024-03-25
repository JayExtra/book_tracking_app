package com.dev.james.booktracker.core.common_models

data class BookProgressData(
    val bookId : String = "",
    val bookImage : String = "",
    val bookTitle : String = "",
    val authors : String = "",
    val description : String = "" ,
    val isUri : Boolean = false,
    val totalPages : Int = 0,
    val totalTimeSpentWeekly : Long = 0L,
    val totalTimeSpent : Long = 0L,
    val totalPagesRead : Int = 0,
    val progress : Float = 0f,
    val currentChapter : Int = 0,
    val currentChapterTitle : String = "",
    val currentPage : Int = 0 ,
    val logs : Map<String , Long> = mapOf() ,
    val bestTime : String = "0s" ,
    val pagesPerMinute : Int = 0
)
