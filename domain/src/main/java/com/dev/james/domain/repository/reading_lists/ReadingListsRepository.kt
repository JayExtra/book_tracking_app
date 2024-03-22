package com.dev.james.domain.repository.reading_lists

import com.dev.james.booktracker.core.common_models.ReadingListItem
import kotlinx.coroutines.flow.Flow

interface ReadingListsRepository {
    suspend fun getReadingLists() : Flow<List<ReadingListItem>>

    suspend fun createReadingList(readingListItem: ReadingListItem)

    suspend fun deleteReadingList(id : String)
}