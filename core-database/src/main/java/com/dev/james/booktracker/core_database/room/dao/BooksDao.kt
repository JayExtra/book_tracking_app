package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.james.booktracker.core.entities.BookEntity
import com.dev.james.booktracker.core.entities.ReadingListEntity
import com.dev.james.booktracker.core.entities.updates.ReadingListBookUpdate
import kotlinx.coroutines.flow.Flow



@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books_table WHERE id =:id")
    suspend fun getBook(id : String) : BookEntity


    @Query("DELETE FROM books_table WHERE id =:id")
    suspend fun deleteBook(id: String)

    @Query("SELECT * FROM books_table")
    fun getAllBooks() : Flow<List<BookEntity>>


    @Query("SELECT * FROM books_table WHERE id =:id")
    suspend fun getSavedBook(id : String) : BookEntity

    @Update(entity = ReadingListEntity::class)
    suspend fun addBookToReadingList(readingListBookUpdate: ReadingListBookUpdate)

}