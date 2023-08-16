package com.dev.james.booktracker.core.common_models

data class BookGoal(
    val bookId : String ,
    val isChapterGoal : Boolean ,
    val goalInfo : String ,
    val isTimeGoal : Boolean ,
    val goalSet : String ,
    val goalPeriod : String ,
    val specificDays : List<String>
)
