package com.dev.james.booktracker.core.common_models

import android.net.Uri

data class Book(
    val bookId : String? = null,
    val bookImage : String? = null,
    val bookTitle : String? = null,
    val bookAuthors : List<String>? = null,
    val bookPagesCount : Int? = null ,
    val publisher : String? = null ,
    val publishedDate : String? = null ,
    val bookUri : Uri = Uri.EMPTY
)
