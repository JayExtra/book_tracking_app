package com.dev.james.booktracker.home.data.datasource

import com.dev.james.booktracker.core_database.room.dao.ReadAndGoalsDao
import com.dev.james.booktracker.core_database.room.entities.BookEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class BooksAndGoalsLocalDataSourceImpl @Inject constructor(
    private val booksAndGoalsDao: ReadAndGoalsDao ,
    private val dispatcher : CoroutineDispatcher = Dispatchers.IO
) : BooksAndGoalsLocalDataSource {
    companion object {
        const val TAG = "BooksAndGoalsLocalDataSourceImpl"
    }
    override suspend fun addBookToDataBase(bookEntity: BookEntity, onBookAdded: (Boolean) -> Unit) {
        try {
            withContext(dispatcher){
                booksAndGoalsDao.addBook(bookEntity)
                val addedBook = booksAndGoalsDao.getBook(bookEntity.bookId)
                if(addedBook.bookId.isNotBlank()){
                    onBookAdded(true)
                }else{
                    onBookAdded(false)
                }
            }
        }catch (e : IOException){
            Timber.tag(TAG).d(e.localizedMessage)
        }
    }
}