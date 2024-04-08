package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookStatisticsData
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.PdfBookItem
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.utilities.convertToOrganisedString

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

fun Book.mapToPresentation() : BaseBookData {
    return BaseBookData(
        id = bookId ?: "" ,
        image = bookImage ?: "" ,
        title = bookTitle ?: "" ,
        authors = bookAuthors?.convertToOrganisedString() ?: "" ,
        description = description ?: "",
        publisher = publisher ?: ""  ,
        publishedDate = publishedDate ?: "" ,
        pages = bookPagesCount ?: 0 ,
        category = category ?: emptyList()
    )
}

fun BookStatisticsData.mapToLibraryData() : LibraryBookData {
   return LibraryBookData(
       id = this.bookId ,
       image = this.bookImage ,
       title = this.bookTitle ,
       author = this.authors ,
       progress = (this.progress * 100).toInt() ,
       pagesRead = this.currentPage ,
       pages = this.totalPages
    )
}