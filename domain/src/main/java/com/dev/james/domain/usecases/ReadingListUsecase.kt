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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadingListUsecase @Inject constructor(
    private val readingListsRepository: ReadingListsRepository ,
    private val fetchActiveBookProgress: FetchActiveBookProgress ,
   // private val defaultDispatcher : CoroutineDispatcher = Dispatchers.IO
) {


    @SuppressLint("NewApi")
    suspend fun fetch() : Flow<List<ReadingLists>> = withContext(Dispatchers.IO) {
        return@withContext readingListsRepository.getReadingLists()
            .map { list ->
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
    }
}