package com.dev.james.data.repositories.reading_lists

import android.database.sqlite.SQLiteException
import com.dev.james.booktracker.core.common_models.ReadingListItem
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.common_models.mappers.toEntity
import com.dev.james.domain.datasources.reading_lists.ReadingListLocalDatasource
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class ReadingListRepositoryImpl @Inject constructor(
    private val readingListLocalDatasource: ReadingListLocalDatasource ,
    private val defaultDispatcher : CoroutineDispatcher = Dispatchers.IO
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

        return if(readingListFlow.first().isEmpty()){
            flow { emit(emptyList()) }
        }else{
            readingListFlow
        }
    }

    override suspend fun createReadingList(readingListItem: ReadingListItem) {
        try {
            readingListLocalDatasource.createReadingList(
                readingListItem.toEntity()
            )
        }catch (e : SQLiteException){
            Timber.tag(TAG).e("Error on adding reading list : ${e.stackTrace}")
        }
    }

    override suspend fun deleteReadingList(id: String) {
        try {
            readingListLocalDatasource.deleteReadingList(id)
        }catch ( e : SQLiteException){
            Timber.tag(TAG).e("Error on deleting reading list : ${e.stackTrace}")
        }
    }
}