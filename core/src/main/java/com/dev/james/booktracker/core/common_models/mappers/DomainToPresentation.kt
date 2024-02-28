package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.PdfBookItem

/*fun BookSave.mapToPresentation() : Book {
    return Book(
        bookId = bookId ,
        bookImage = bookImage ,
        bookTitle = bookTitle ,
        //update this to support change from json to list
        bookAuthors = null ,
        bookPagesCount = bookPagesCount ,
        publisher = publisher ,
        publishedDate = publishedDate
    )
}*/

fun PdfBookItem.mapToPresentation() : Book {
    return Book(
        bookId = null ,
        bookImage = null ,
        bookTitle = title ,
        bookAuthors = listOf(author) ,
        bookPagesCount = pages ,
        publisher = publisher ,
        publishedDate = date ,
        bookUri = bookUri
    )
}