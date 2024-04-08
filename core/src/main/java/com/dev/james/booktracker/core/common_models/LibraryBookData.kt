package com.dev.james.booktracker.core.common_models

data class LibraryBookData(
    val id: String = "",
    val image: String = "",
    val title: String = "" ,
    val author: String = "",
    val progress: Int = 0 ,
    val pagesRead : Int = 0 ,
    val pages : Int = 0
)
