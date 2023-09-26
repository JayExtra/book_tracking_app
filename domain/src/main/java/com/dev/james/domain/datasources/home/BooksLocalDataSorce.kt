package com.dev.james.domain.datasources.home
import com.dev.james.booktracker.core.entities.BookEntity
import kotlinx.coroutines.flow.Flow

interface BooksLocalDataSource {
    suspend fun addBookToDataBase(bookEntity : BookEntity, onBookAdded : (Boolean, String?) -> Boolean ) : Boolean
    suspend fun deleteBookFromDataBase(bookId : String , onBookDeleted : (Boolean) -> Boolean ) : Boolean
    fun getAllBooks() : Flow<List<BookEntity>>

    suspend fun getCachedBook(id : String) : BookEntity
}