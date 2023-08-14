package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookSave

fun BookSave.mapToPresentation() : Book {
    return Book(
        bookId = bookId ,
        bookImage = bookImage ,
        bookTitle = bookTitle ,
        //update this to support change from json to list
        bookAuthors = null  ,
        bookSmallThumbnail = bookSmallThumbnail ,
        bookPagesCount = bookPagesCount ,
        publisher = publisher ,
        publishedDate = publishedDate
    )
}