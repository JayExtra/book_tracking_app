package com.dev.james.domain.datasources.reading_lists

import com.dev.james.booktracker.core.entities.ReadingListEntity
import kotlinx.coroutines.flow.Flow

interface ReadingListLocalDatasource {
    suspend fun createReadingList(readingListEntity: ReadingListEntity)
    fun getReadingLists() : Flow<List<ReadingListEntity>>

    suspend fun deleteReadingList(id : String)

    suspend fun getAReadingList(id : String) : ReadingListEntity
}