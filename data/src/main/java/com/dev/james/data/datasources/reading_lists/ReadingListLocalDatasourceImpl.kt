package com.dev.james.data.datasources.reading_lists

import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.datastore.proto.PendingReadingList
import com.dev.james.booktracker.core.datastore.proto.PendingReadingListItems
import com.dev.james.booktracker.core.entities.ReadingListEntity
import com.dev.james.booktracker.core_database.room.dao.ReadingListDao
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.domain.datasources.reading_lists.ReadingListLocalDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadingListLocalDatasourceImpl @Inject constructor(
    private val readingListDao: ReadingListDao ,
    private val dataStoreManager: DataStoreManager
) : ReadingListLocalDatasource {
    override suspend fun createReadingList(readingListEntity: ReadingListEntity) {
        readingListDao.addReadingList(readingListEntity)
    }

    override fun getReadingLists(): Flow<List<ReadingListEntity>> {
        return readingListDao.getReadingLists()
    }

    override suspend fun deleteReadingList(id: String) {
        readingListDao.deleteReadingList(id)
    }

    override fun getAReadingList(id: String): Flow<ReadingListEntity> {
        return readingListDao.getAReadingList(id)
    }

    override fun getPendingReadingListItem(): Flow<List<PendingReadingListItems>> {
        return dataStoreManager.pendingReadingListItems()
    }

    override suspend fun addToPendingList(readingListId: String, baseBookData: BaseBookData) {
        dataStoreManager.addToPendingReadingList(readingListId ,  baseBookData)
    }

    override suspend fun removeFromPendingList(readingListId: String, bookId: String) {
        dataStoreManager.removeFromPendingList(readingListId , bookId)
    }
}