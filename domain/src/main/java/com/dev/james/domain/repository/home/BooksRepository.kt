package com.dev.james.domain.repository.home

import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.dto.BookVolumeDto
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun getBooksFromApi(
        bookTitle : String ,
        bookAuthor : String
    ) : Flow<Resource<BookVolumeDto>>
  
    suspend fun saveBookToDatabase(bookSave : BookSave) : Boolean
    suspend fun deleteBookInDatabase(bookId : String) : Boolean

    fun getSavedBooks() : Flow<List<BookSave>>

    suspend fun getSingleSavedBook(id : String) : BookSave

}