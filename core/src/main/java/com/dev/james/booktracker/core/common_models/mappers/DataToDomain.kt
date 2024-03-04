package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.dto.BookDto

fun BookDto.mapToDomainObject(): Book {
    return Book(
        bookId = id,
        bookImage = volumeInfo?.image_links?.thumbnail,
        bookAuthors = volumeInfo?.authors,
        bookTitle = volumeInfo?.title,
        bookPagesCount = volumeInfo?.pageCount,
        publishedDate = volumeInfo?.published_date,
        publisher = volumeInfo?.publisher ,
        description = volumeInfo?.description ,
        category = volumeInfo?.categories
    )
}