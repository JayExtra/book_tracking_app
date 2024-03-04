package com.dev.james.booktracker.core.common_models

data class SuggestedBook(
    val id : String = "" ,
    val image : String = "",
    val title : String = "" ,
    val authors : String = "" ,
    val publisher : String = "" ,
    val publishedDate : String = "" ,
    val pages : Int = 0 ,
    val category : List<String> = listOf()
)
