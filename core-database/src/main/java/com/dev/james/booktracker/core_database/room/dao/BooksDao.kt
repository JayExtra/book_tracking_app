package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.james.booktracker.core_database.room.entities.BookEntity

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(bookEntity: BookEntity)

    @Query("SELECT * FROM books_table WHERE id =:id")
    suspend fun getBook(id : String) : BookEntity
}