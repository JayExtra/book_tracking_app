package com.dev.james.domain.usecases

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.ReadingListItem
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.booktracker.core.common_models.mappers.mapToLibraryData
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ReadingListUsecase @Inject constructor(
    private val readingListsRepository: ReadingListsRepository ,
    private val fetchActiveBookProgress: FetchActiveBookProgress ,
   // private val defaultDispatcher : CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        const val TAG = "ReadingListUsecase"
    }

    @SuppressLint("NewApi")
    suspend fun fetch() : Flow<List<ReadingLists>> = withContext(Dispatchers.IO) {
        val readingLists = readingListsRepository.getReadingLists()
        Timber.tag(TAG).d(readingLists.first().toString())
        val mappedReadingList = readingLists.map { list ->
                list.map { item ->
                    val differedBooks = item.readingList.map {
                        async { fetchActiveBookProgress.invoke(it) }
                    }
                    val booksListWithData = differedBooks.awaitAll().map {
                        it.mapToLibraryData()
                    }

                    ReadingLists(
                        id = item.id ,
                        name = item.name ,
                        image = item.image ,
                        description = item.description ,
                        readingList = booksListWithData ,
                        date = item.date ,
                        starred = item.starred
                    )
                }
            }

        Timber.tag(TAG).d("new list : %s", mappedReadingList.first().toString())

        return@withContext mappedReadingList
    }
}