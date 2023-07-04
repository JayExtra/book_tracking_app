package com.dev.james.booktracker.core.common_models

data class Book(
    val bookId : String,
    val bookImage : String?,
    val bookTitle : String,
    val bookAuthors : List<String>,
    val bookSmallThumbnail : String?,
    val bookPagesCount : Int
)
