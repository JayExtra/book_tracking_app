package com.dev.james.data.repositories.books

import android.database.sqlite.SQLiteException
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.mappers.mapToBookDomainObject
import com.dev.james.booktracker.core.common_models.mappers.mapToBookEntity
import com.dev.james.booktracker.core.common_models.mappers.mapToDomainObject
import com.dev.james.domain.utilities.ConnectivityManager
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.dto.BookVolumeDto
import com.dev.james.booktracker.core.entities.updates.ReadingListBookUpdate
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.domain.datasources.home.BooksApiDataSource
import com.dev.james.domain.datasources.home.BooksLocalDataSource
import com.dev.james.domain.datasources.reading_lists.ReadingListLocalDatasource
import com.dev.james.domain.repository.home.BooksRepository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class BooksRepositoryImpl
@Inject constructor(
    private val booksApiDataSource: BooksApiDataSource,
    private val booksLocalDataSource: BooksLocalDataSource,
    private val dataStoreManager: DataStoreManager,
    private val connectivityManager: ConnectivityManager,
    private val readingListLocalDatasource: ReadingListLocalDatasource ,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BooksRepository {

    companion object {
        const val TAG = "BooksRemoteRepositoryImpl"
    }

    override fun getBooksFromApi(
        bookTitle: String,
        bookAuthor: String
    ): Flow<Resource<BookVolumeDto>> = flow {
        emit(Resource.Loading())
        if (connectivityManager.getNetworkStatus()) {
            try {
                val booksFromApi =
                    withContext(dispatcher) {
                        booksApiDataSource.getQueriedBook(query = bookTitle, author = bookAuthor)
                    }
                Timber.tag(TAG).d("Books => %s", booksFromApi.toString())
                emit(Resource.Success(data = booksFromApi))

            } catch (e: HttpException) {
                if (e.code() == 429) {
                    Timber.tag(TAG).e("${e.message()} : Too many requests")
                } else {
                    emit(
                        Resource.Error(
                            message = "Oops , seems the problem is in our side. Please be patient as we try to fix."
                        )
                    )
                }
            }
            catch (e : SocketTimeoutException){
                Timber.tag(TAG).e("getBookByCategory Error => %s", e.toString())
                emit(Resource.Error(message = "Oops! The server took to long to respond. Please try again."))
            }
        } else {
            emit(
                Resource.Error(
                    message = "Oops , seems the problem is in your side. Please reconnect your device network and try again."
                )
            )
        }
    }

    override suspend fun saveBookToDatabase(bookSave: BookSave): Boolean {
        return booksLocalDataSource.addBookToDataBase(bookSave.mapToBookEntity()) { isAdded, _ ->
            isAdded
        }
    }

    override suspend fun deleteBookInDatabase(bookId: String): Boolean {
        return booksLocalDataSource.deleteBookFromDataBase(bookId) { isDeleted -> isDeleted }
    }

    override fun getSavedBooks(): Flow<List<BookSave>> {
        return try {

            val booksFlow = booksLocalDataSource.getAllBooks().map { booksEntityList ->
                booksEntityList.map { bookEntity ->
                    bookEntity.mapToBookDomainObject()
                }
            }
            booksFlow
        } catch (e: IOException) {
            Timber.tag(TAG).d(e.localizedMessage as String)
            flow { emit(emptyList<BookSave>()) }
        } catch (e: SQLiteException) {
            Timber.tag(TAG).d(e.localizedMessage as String)
            flow { emit(emptyList<BookSave>()) }
        }
    }

    override suspend fun getSingleSavedBook(id: String): BookSave {
        return try {
            val book = booksLocalDataSource.getCachedBook(id)
            Timber.tag(TAG).d("book saved => $book")
            book.mapToBookDomainObject()
        } catch (e: IOException) {
            Timber.e("Could not fetch book from the database.Issue : ${e.message}")
            BookSave()
        } catch (e: SQLiteException) {
            Timber.e("Could not fetch book from the database.Issue : ${e.message}")
            BookSave()
        }
    }

    override suspend fun setAsActiveBook(
        bookId: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            dataStoreManager.storeStringValue(DataStorePreferenceKeys.CURRENT_ACTIVE_BOOK_ID , bookId)
            Resource.Success(data = true)
            onSuccess(true)
        }catch (e : IOException){
            Timber.e("Could not set as active book into datastore : ${e.message}")
            onFailure("Could not set as active book into datastore : ${e.message}")
        }
    }

    override suspend fun getActiveBookId(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        try {
            val bookId = dataStoreManager.readStringValueOnce(DataStorePreferenceKeys.CURRENT_ACTIVE_BOOK_ID)
            onSuccess(bookId)
        }catch (e : IOException){
            Timber.e("Could not get active book into datastore : ${e.message}")
           // Resource.Error( message = "${e.message}")
            onFailure("Could not get active book into datastore : ${e.message}")
        }
    }

    override suspend fun unsetActiveBook(
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            dataStoreManager.storeStringValue(DataStorePreferenceKeys.CURRENT_ACTIVE_BOOK_ID , "")
            onSuccess(true)
        }catch (e : IOException){
            Timber.e("Could not unset active book into datastore : ${e.message}")
            onFailure("Could not get active book into datastore : ${e.message}")
        }
    }

    override suspend fun getBookByCategory(category: String): Resource<List<Book>> {
        return if(connectivityManager.getNetworkStatus()){
            try {
                val query = "subject:$category"
                val booksFromApi = booksApiDataSource.queryByCategory(query)

                Timber.tag(TAG).d("Books => %s", booksFromApi.toString())
                Resource.Success(data = booksFromApi.items?.map { it.mapToDomainObject() })
            } catch (e: HttpException) {
                Timber.tag(TAG).e("getBookByCategory Error => %s", e.toString())
                Resource.Error(message = "Oops , seems the problem is in our side. Please be patient as we try to fix.")
            } catch (e : SocketTimeoutException){
                Timber.tag(TAG).e("getBookByCategory Error => %s", e.toString())
                Resource.Error(message = "Oops! The server took to long to respond. Please try again.")
            }
        }else {
            Timber.tag(TAG).d("Books => %s", "Please reconnect your device network and try again." )
            Resource.Error(message = "Please reconnect your device network and try again.")
        }
    }

    override suspend fun addOrRemoveBookFromReadingList(readingListId: String, bookId: String): Resource<String> {
        return try {
            val cachedReadingList = readingListLocalDatasource.getAReadingList(id = readingListId)
            val copiedList = cachedReadingList.first().readingList.toMutableList()

            if (copiedList.contains(bookId)){
                    Timber.tag(TAG).d("Book $bookId exists in $readingListId reading list , removing it")
                    val bookIndex = copiedList.indexOf(bookId)
                    copiedList.removeAt(bookIndex)
                    booksLocalDataSource.addBookToReadingList(
                        ReadingListBookUpdate(
                            id = readingListId ,
                            readingList = copiedList.toImmutableList()
                        )
                    )
                    return Resource.Success(data = "removed:$bookId")
                } else {
                    Timber.tag(TAG).d("Book $bookId does not exist in $readingListId reading list , adding it")
                    copiedList.add(bookId)
                    booksLocalDataSource.addBookToReadingList(
                        ReadingListBookUpdate(
                            id = readingListId ,
                            readingList = copiedList.toImmutableList()
                        )
                    )
                    return Resource.Success(data = "added:$bookId")
                }

        }catch (e : SQLiteException){
            Timber.tag(TAG).d("Error on adding or removing book to reading list: ${e.stackTrace}")
            Resource.Error("error:could not add or remove book from playlist =>${e.localizedMessage}")
        }
    }

    override suspend fun addOrRemoveFromWishList(baseBookData: BaseBookData): String {
        val currentWishList = dataStoreManager.getWishList().first().wishlist
        Timber.tag(TAG).d("current wish list => ${currentWishList.toString()}")

        return if(currentWishList.contains(baseBookData)){
            Timber.tag(TAG).d("removing => ${baseBookData.title}")
            dataStoreManager.removeFromWishList(baseBookData.id)
            if (!dataStoreManager.getWishList().first().wishlist.contains(baseBookData)) {
                Timber.tag(TAG).d("removed => ${baseBookData.title}")
                "removed:${baseBookData.title}"
            }else {
                Timber.tag(TAG).d("fail to remove => ${baseBookData.title}")
                "failed:${baseBookData.title}"
            }
        }else {
            Timber.tag(TAG).d("adding => ${baseBookData.title}")
            dataStoreManager.addToWishList(baseBookData)
            if (dataStoreManager.getWishList().first().wishlist.contains(baseBookData)) {
                Timber.tag(TAG).d("added => ${baseBookData.title}")
                "added:${baseBookData.title}"
            }else {
                Timber.tag(TAG).d("failed to add => ${baseBookData.title}")
                "failed:${baseBookData.title}"
            }
        }
    }

    override fun getWishList(): Flow<List<BaseBookData>> {
        return dataStoreManager.getWishList()
            .map {
                it.wishlist
            }.catch { e ->
                Timber.tag(TAG).e(e.stackTrace.toString())
            }
    }
}


