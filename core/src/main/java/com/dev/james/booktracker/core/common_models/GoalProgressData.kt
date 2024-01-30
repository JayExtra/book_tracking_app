package com.dev.james.booktracker.core.common_models

data class GoalProgressData(
    val goalId : String = "" ,
    val goalInfo : String = "",
    val goalTime : Long = 0L,
    val goalPeriod : String = "" ,
    val booksToRead : Int = 0 ,
    val booksRead : Int = 0,
    val weeklyLogData : Map<String , Long> = mapOf()
)
