package com.dev.james.booktracker.home.domain.repositories

import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core_network.dtos.BookVolumeDto
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