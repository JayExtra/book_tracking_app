package com.dev.james.booktracker.core.common_models.mappers

data class BookSave(
    val bookId : String,
    val bookImage : String,
    val bookTitle : String,
    val bookAuthors : String,
    val bookSmallThumbnail : String,
    val bookPagesCount : Int ,
    val publisher : String ,
    val publishedDate : String ,
    val isUri : Boolean ,
    val currentChapter : Int ,
    val currentChapterTitle : String ,
    val chapters : Int
)
