package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core_network.dtos.BookDto

fun BookDto.mapToBookDomainObject() : Book {
    return Book(
        bookId = id ,
        bookImage = volumeInfo?.image_links?.thumbnail ,
        bookAuthors = volumeInfo?.authors ,
        bookTitle = volumeInfo?.title ,
        bookSmallThumbnail = volumeInfo?.image_links?.small_thumbnail ,
        bookPagesCount = volumeInfo?.pageCount ,
        bookThumbnail = volumeInfo?.image_links?.thumbnail,
        publishedDate = volumeInfo?.published_date ,
        publisher = volumeInfo?.publisher
    )
}