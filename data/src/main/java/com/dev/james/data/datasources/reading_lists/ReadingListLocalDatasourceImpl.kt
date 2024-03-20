package com.dev.james.data.datasources.reading_lists

import com.dev.james.booktracker.core.entities.ReadingListEntity
import com.dev.james.booktracker.core_database.room.dao.ReadingListDao
import com.dev.james.domain.datasources.reading_lists.ReadingListLocalDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadingListLocalDatasourceImpl @Inject constructor(
    private val readingListDao: ReadingListDao
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

    override suspend fun getAReadingList(id: String): ReadingListEntity {
        return readingListDao.getAReadingList(id)
    }
}