package com.dev.james.domain.datasources.reading_lists

import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.datastore.proto.PendingReadingList
import com.dev.james.booktracker.core.datastore.proto.PendingReadingListItems
import com.dev.james.booktracker.core.entities.ReadingListEntity
import kotlinx.coroutines.flow.Flow

interface ReadingListLocalDatasource {
    suspend fun createReadingList(readingListEntity: ReadingListEntity)
    fun getReadingLists() : Flow<List<ReadingListEntity>>

    suspend fun deleteReadingList(id : String)

    fun getAReadingList(id : String) : Flow<ReadingListEntity>

    fun getPendingReadingListItem() : Flow<List<PendingReadingListItems>>

    suspend fun addToPendingList(readingListId : String , baseBookData: BaseBookData)

    suspend fun removeFromPendingList(readingListId: String , bookId : String)

}