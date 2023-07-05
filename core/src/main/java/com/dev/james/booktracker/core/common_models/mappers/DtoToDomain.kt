package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core_network.dtos.BookDto

fun BookDto.mapToBookDomainObject() : Book {
    return Book(
        bookId = id ,
        bookImage = volumeInfo.imageLinks?.thumbnail ,
        bookAuthors = volumeInfo.authors ,
        bookTitle = volumeInfo.title ,
        bookSmallThumbnail = volumeInfo.imageLinks?.smallThumbnail ,
        bookPagesCount = volumeInfo.pageCount ,
        bookThumbnail = volumeInfo.imageLinks?.thumbnail
    )
}