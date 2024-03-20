package com.dev.james.domain.repository.home

import com.dev.james.booktracker.core.common_models.Book
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

    suspend fun setAsActiveBook(bookId : String , onSuccess : (Boolean) -> Unit , onFailure: (String) -> Unit)

    suspend fun getActiveBookId(onSuccess: (String) -> Unit , onFailure: (String) -> Unit)

    suspend fun unsetActiveBook(onSuccess : (Boolean) -> Unit , onFailure : (String) -> Unit)

    suspend fun getBookByCategory(category : String) : Resource<List<Book>>

    suspend fun addBookToReadingList( readingListId : String , bookId : String) : Resource<String>

    suspend fun removeBookFromReadingList(readingListId: String , bookId: String) : Resource<String>

}