package com.dev.james.booktracker.core.common_models

data class Goal(
    val goalId : String ,
    val goalInfo : String ,
    val goalTime : Long ,
    val goalPeriod : String ,
    val specificDays : List<String> ,
    val shouldShowAlert : Boolean ,
    val booksToRead : Int ,
    val booksRead : Int,
    val alertNote : String ,
    val alertTime : String
)
