package com.dev.james.data.repositories.books

import android.database.sqlite.SQLiteException
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.domain.utilities.ConnectivityManager
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.entities.BookEntity
import com.dev.james.booktracker.core.dto.BookVolumeDto
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.domain.datasources.home.BooksApiDataSource
import com.dev.james.domain.datasources.home.BooksLocalDataSource
import com.dev.james.domain.repository.home.BooksRepository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class BooksRepositoryImpl
    @Inject constructor(
        private val booksApiDataSource: BooksApiDataSource,
        private val booksLocalDataSource : BooksLocalDataSource,
        private val connectivityManager: ConnectivityManager,
        private val dispatcher : CoroutineDispatcher = Dispatchers.IO
    ): BooksRepository {

    companion object {
        const val TAG = "BooksRemoteRepositoryImpl"
    }

    override fun getBooksFromApi(bookTitle: String, bookAuthor: String): Flow<Resource<BookVolumeDto>> = flow {
        emit(Resource.Loading())
        if(connectivityManager.getNetworkStatus()){
            try{
                val booksFromApi =
                    withContext(dispatcher) {
                        booksApiDataSource.getQueriedBook(query = bookTitle , author = bookAuthor)
                    }
                emit(Resource.Success(data = booksFromApi))

            }catch (e : HttpException){
                if(e.code() == 429){
                   Timber.tag(TAG).e("${e.message()} : Too many requests")
                }else{
                    emit(Resource.Error(
                        message = "Oops , seems the problem is in our side. Please be patient as we try to fix."
                    ))
                }
            }
        }else{
            emit(Resource.Error(
                message = "Oops , seems the problem is in your side. Please reconnect your device network and try again."
            ))
        }
    }

    override suspend fun saveBookToDatabase(bookSave: BookSave): Boolean {
           return  booksLocalDataSource.addBookToDataBase( bookSave.mapToBookEntity()){
                   isAdded , _ -> isAdded
           }
    }

    override suspend fun deleteBookInDatabase(bookId: String): Boolean {
       return booksLocalDataSource.deleteBookFromDataBase(bookId){ isDeleted -> isDeleted }
    }

    override fun getSavedBooks(): Flow<List<BookSave>> {
         return try {

         val booksFlow = booksLocalDataSource.getAllBooks().map { booksEntityList ->
                booksEntityList.map { bookEntity ->
                    bookEntity.mapToBookDomainObject()
                }
          }
             booksFlow
        }catch (e : IOException){
            Timber.tag(TAG).d(e.localizedMessage as String)
            flow {emit(emptyList<BookSave>()) }
        }catch (e : SQLiteException){
             Timber.tag(TAG).d(e.localizedMessage as String)
             flow { emit(emptyList<BookSave>())}
        }
    }

    override suspend fun getSingleSavedBook(id: String): BookSave {
        return try {
            booksLocalDataSource.getCachedBook(id)
                .mapToBookDomainObject()
        }catch (e : IOException){
            Timber.e("Could not fetch book from the database.Issue : ${e.message}")
            BookSave()
        }catch ( e : SQLiteException){
            Timber.e("Could not fetch book from the database.Issue : ${e.message}")
            BookSave()
        }
    }
}

fun BookSave.mapToBookEntity() : BookEntity {
    return BookEntity(
        bookId = bookId,
        bookImage = bookImage ,
        isUri = isUri,
        bookAuthors = bookAuthors ,
        bookTitle = bookTitle,
        bookSmallThumbnail = bookSmallThumbnail ,
        bookPagesCount = bookPagesCount ,
        publisher = publisher ,
        publishedDate = publishedDate ,
        currentChapterTitle = currentChapterTitle ,
        chapters = chapters ,
        currentChapter = currentChapter
    )
}

fun BookEntity.mapToBookDomainObject() : BookSave {
    return BookSave(
        bookId = bookId,
        bookImage = bookImage ,
        isUri = isUri,
        bookAuthors = bookAuthors ,
        bookTitle = bookTitle,
        bookSmallThumbnail = bookSmallThumbnail ,
        bookPagesCount = bookPagesCount ,
        publisher = publisher ,
        publishedDate = publishedDate ,
        currentChapterTitle = currentChapterTitle ,
        chapters = chapters ,
        currentChapter = currentChapter
    )
}