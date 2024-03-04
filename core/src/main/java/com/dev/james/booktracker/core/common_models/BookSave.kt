package com.dev.james.booktracker.core.common_models

data class BookSave(
    val bookId : String = "",
    val bookImage : String = "",
    val bookTitle : String = "",
    val bookAuthors : String = "",
    val bookDescription : String = "" ,
    val bookSmallThumbnail : String = "",
    val bookPagesCount : Int = 0 ,
    val publisher : String = ""  ,
    val publishedDate : String = "" ,
    val isUri : Boolean = false ,
    val category:String = "" ,
    val chapters : Int = 0
)
