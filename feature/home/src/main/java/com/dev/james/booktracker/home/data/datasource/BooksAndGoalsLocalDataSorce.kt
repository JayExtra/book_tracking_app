package com.dev.james.booktracker.home.data.datasource

import com.dev.james.booktracker.core_database.room.entities.BookEntity

interface BooksLocalDataSource {
    suspend fun addBookToDataBase(bookEntity : BookEntity , onBookAdded : suspend (Boolean) -> Unit)
}