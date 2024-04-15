package com.dev.james.data.repositories.reading_lists

import android.database.sqlite.SQLiteException
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.common_models.ReadingListItem
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.common_models.mappers.toEntity
import com.dev.james.booktracker.core.datastore.proto.PendingReadingListItems
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.domain.datasources.reading_lists.ReadingListLocalDatasource
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ReadingListRepositoryImpl @Inject constructor(
    private val readingListLocalDatasource: ReadingListLocalDatasource,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReadingListsRepository {

    companion object {
        const val TAG = "ReadingListRepositoryImpl"
    }

    override suspend fun getReadingLists(): Flow<List<ReadingListItem>> {
        val readingListFlow = readingListLocalDatasource.getReadingLists()
            .map { list ->
                list.map { readingListEntity ->
                    readingListEntity.toDomain()
                }
            }

        return if (readingListFlow.first().isEmpty()) {
            Timber.tag(TAG).e("No reading lists available")
            flow { emit(emptyList()) }
        } else {
            Timber.tag(TAG).e("Reading lists available : ${readingListFlow.first().toString()}")
            readingListFlow
        }
    }

    override suspend fun createReadingList(readingListItem: ReadingListItem) {
        try {
            readingListLocalDatasource.createReadingList(
                readingListItem.toEntity()
            )
            readingListLocalDatasource.createPendingReadingList(readingListItem.id)

        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("Error on adding reading list : ${e.stackTrace}")
        }
    }

    override suspend fun deleteReadingList(id: String) {
        try {
            readingListLocalDatasource.deleteReadingList(id)
        } catch (e: SQLiteException) {
            Timber.tag(TAG).e("Error on deleting reading list : ${e.stackTrace}")
        }
    }

    override fun getSingleReadingList(id: String): Flow<ReadingListItem> {
        return readingListLocalDatasource.getAReadingList(id)
            .map { readingListEntity ->
                readingListEntity.toDomain()
            }
    }

    override fun getPendingReadingListItem(readingListId: String): Flow<PendingReadingListItems> {
        return readingListLocalDatasource.getPendingReadingListItem()
            .map {
                it.find { item ->
                    item.readingListId == readingListId
                } ?: PendingReadingListItems()
            }
    }

    override suspend fun addToPendingList(
        readingListId: String,
        book: BaseBookData
    ): Resource<Boolean> {
        return try {
            readingListLocalDatasource.addToPendingList(
                readingListId ,
                book
            )
            val readingList = readingListLocalDatasource.getPendingReadingListItem()
                .first().find {
                    it.readingListId == readingListId
                }!!

            return if (readingList.books.contains(book)){
                Timber.tag(TAG).e("successfully added to pending list")
                Resource.Success(data = true)
            }else {
                Timber.tag(TAG).e("could not add to pending list")
                Resource.Success(data = false)
            }

        }catch (e : IOException){
            Timber.tag(TAG).e("error: could not add to pending reading list => ${e.stackTrace}")
            Resource.Error(message = "error: could not add to pending reading list => ${e.message}")
        }
    }

    override suspend fun removeFromPendingList(
        readingListId: String,
        bookId: String
    ): Resource<Boolean> {
        return try {
            readingListLocalDatasource.removeFromPendingList(
                readingListId ,
                bookId
            )
            val books = readingListLocalDatasource.getPendingReadingListItem()
                .first().find {
                    it.readingListId == readingListId
                }!!.books.map { it.id }

            return if (!books.contains(bookId)){
                Timber.tag(TAG).e("successfully removed from pending list")
                Resource.Success(data = true)
            }else {
                Timber.tag(TAG).e("could not remove from pending list")
                Resource.Success(data = false)
            }

        }catch (e : IOException){
            Timber.tag(TAG).e("error: could not remove to pending reading list => ${e.stackTrace}")
            Resource.Error(message = "error: could not remove to pending reading list => ${e.message}")
        }
    }
}