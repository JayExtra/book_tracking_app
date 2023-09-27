package com.dev.james.data.datasources.books

import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core.entities.BookEntity
import com.dev.james.domain.datasources.home.BooksLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
        onBookAdded: (Boolean, String?) -> Boolean
    ): Boolean {

        return try {
            withContext(dispatcher){
                booksDao.addBook(bookEntity)
                val addedBook = booksDao.getBook(bookEntity.bookId)
                if(addedBook.bookId.isNotBlank()){
                    onBookAdded(true , null)
                }else{
                    onBookAdded(false , null)
                }
            }

        }catch (e : IOException){
            Timber.tag(TAG).d(e.localizedMessage)
            onBookAdded(false , e.localizedMessage?.toString())
        }
    }

    override suspend fun deleteBookFromDataBase(
        bookId: String,
        onBookDeleted: (Boolean) -> Boolean
    ): Boolean {
        return try {
            withContext(dispatcher){
                booksDao.deleteBook(bookId)
                booksDao.getBook(bookId)
                onBookDeleted(true)

            }
        }catch (e : IOException){
            Timber.tag(TAG).d(e.localizedMessage)
            false
        }
    }

    override fun getAllBooks(): Flow<List<BookEntity>> {
        return booksDao.getAllBooks()
    }

    override suspend fun getCachedBook(id: String): BookEntity {
        return booksDao.getBook(id)
    }

}