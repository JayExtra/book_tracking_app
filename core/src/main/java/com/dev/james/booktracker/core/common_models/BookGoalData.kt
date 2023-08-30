package com.dev.james.booktracker.core.common_models

data class BookGoalData(
    val bookId : String = "" ,
    val bookImage : String = "" ,
    val isUri : Boolean = false ,
    val totalPages : Int = 0 ,
    val totalTimeSpent : Long = 0L ,
    val totalPagesRead : Int = 0 ,
    val logs : List<BookGoalLog> = emptyList()
)
