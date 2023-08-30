package com.dev.james.booktracker.home.domain.datasources
import com.dev.james.booktracker.core_database.room.entities.BookEntity
import kotlinx.coroutines.flow.Flow

interface BooksLocalDataSource {
    suspend fun addBookToDataBase(bookEntity : BookEntity , onBookAdded : (Boolean , String?) -> Boolean ) : Boolean
    suspend fun deleteBookFromDataBase(bookId : String , onBookDeleted : (Boolean) -> Boolean ) : Boolean
    fun getAllBooks() : Flow<List<BookEntity>>

    suspend fun getCachedBook(id : String) : BookEntity
}