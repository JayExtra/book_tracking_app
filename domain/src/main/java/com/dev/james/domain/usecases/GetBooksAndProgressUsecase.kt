package com.dev.james.domain.usecases

import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.home.LogsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

class GetBooksAndProgressUsecase @Inject constructor(
    private val booksRepository: BooksRepository ,
    private val logsRepository: LogsRepository
) {

    companion object {
        const val TAG = "GetBooksAndProgressUsecase"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke() : Flow<List<LibraryBookData>> {
        return booksRepository.getSavedBooks()
            .map { booksList ->
                booksList.map { book ->
                   /* val latestLog =  withContext(Dispatchers.IO){
                        val logDiffered = async { logsRepository.getRecentBookLog(book.bookId) }
                        logDiffered.await()
                    }*/
                    val latestLog = logsRepository.getRecentBookLog(book.bookId)
                    LibraryBookData(
                        id = book.bookId ,
                        image = book.bookImage ,
                        title = book.bookTitle ,
                        author = book.bookAuthors ,
                        progress = calculateProgress(
                            totalPages = book.bookPagesCount ,
                            totalPagesRead = latestLog.currentPage
                        ).calculatePercentageProgress()
                    )
                }
            }.catch { t ->
                Timber.tag(TAG).e("GetBooksAndProgressUsecase => ${t.localizedMessage}")
            }

        /*val booksFlow = booksRepository.getSavedBooks()
            .flatMapConcat { booksList ->
                booksList.asFlow()
            }.map { book ->
                val latestLog = logsRepository.getRecentBookLog(book.bookId)
                LibraryBookData(
                    id = book.bookId ,
                    image = book.bookImage ,
                    title = book.bookTitle ,
                    author = book.bookAuthors ,
                    progress = calculateProgress(
                        totalPages = book.bookPagesCount ,
                        totalPagesRead = latestLog.currentPage
                    ).calculatePercentageProgress()
                )
            }*/
    }

    private fun calculateProgress(
        totalPages: Int,
        totalPagesRead: Int
    ): Float {
        return ((totalPagesRead.toFloat() / totalPages.toFloat()) * 10.0f).roundToInt() / 10.0f
    }

    private fun Float.calculatePercentageProgress() : Int {
        return if(this == 0f){
            0
        }else {
            (this * 100).toInt()
        }
    }
}