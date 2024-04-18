package com.dev.james.domain.repository.reading_lists

import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.ReadingListItem
import com.dev.james.booktracker.core.datastore.proto.PendingReadingListItems
import com.dev.james.booktracker.core.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface ReadingListsRepository {
    suspend fun getReadingLists() : Flow<List<ReadingListItem>>

    suspend fun createReadingList(readingListItem: ReadingListItem)

    suspend fun deleteReadingList(id : String)

    fun getSingleReadingList(id : String) : Flow<ReadingListItem>

    fun getPendingReadingListItem(readingListId : String) : Flow<PendingReadingListItems>

    suspend fun addToPendingList(readingListId : String , book: BaseBookData) : Resource<Boolean>

    suspend fun removeFromPendingList(readingListId: String , bookId: String) : Resource<Boolean>
}