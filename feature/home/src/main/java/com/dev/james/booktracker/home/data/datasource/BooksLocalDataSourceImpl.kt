package com.dev.james.booktracker.home.data.datasource

import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.entities.BookEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class BooksLocalDataSourceImpl @Inject constructor(
    private val booksDao: BooksDao,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO
) : BooksLocalDataSource {
    companion object {
        const val TAG = "BooksAndGoalsLocalDataSourceImpl"
    }
    override suspend fun addBookToDataBase(
        bookEntity: BookEntity,
        onBookAdded: suspend (Boolean) -> Unit
    ) {
        try {
                booksDao.addBook(bookEntity)
                val addedBook = booksDao.getBook(bookEntity.bookId)
                if(addedBook.bookId.isNotBlank()){
                    onBookAdded(true)
                }else{
                    onBookAdded(false)
                }

        }catch (e : IOException){
            Timber.tag(TAG).d(e.localizedMessage)
        }
    }
}