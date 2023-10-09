package com.dev.james.domain.usecases

import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.home.LogsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.roundToInt

class FetchActiveBookProgress @Inject constructor(
    private val booksRepository: BooksRepository,
    private val logsRepository: LogsRepository ,
    private val dataSoreManager : DataStoreManager
) {

    suspend operator fun invoke() : BookProgressData {
            val activeBookId = dataSoreManager.readStringValueOnce(DataStorePreferenceKeys.CURRENT_ACTIVE_BOOK_ID)
            val cachedBook = getCachedBook(activeBookId)
            val bookLogs = getBookGoalLogs(activeBookId)
            val totalPages = cachedBook.bookPagesCount

        return if (bookLogs.isNotEmpty()){

            val totalPagesRead = bookLogs.calculateTotalPagesRead()
            val totalTimeSpent = bookLogs.calculateTotalTimeSpent()
            val mostRecentLog = bookLogs.getMostRecentLog()

            BookProgressData(
                bookId = activeBookId ,
                bookImage = cachedBook.bookImage ,
                bookTitle = cachedBook.bookTitle,
                isUri = cachedBook.isUri ,
                totalPages = totalPages,
                totalTimeSpent = totalTimeSpent ,
                totalPagesRead = totalPagesRead ,
                currentChapterTitle = if(mostRecentLog.logId.isNotBlank()) mostRecentLog.currentChapterTitle else cachedBook.currentChapterTitle ,
                currentChapter = if(mostRecentLog.logId.isNotBlank()) mostRecentLog.currentChapter else cachedBook.currentChapter,
                logs = bookLogs ,
                progress = calculateProgress(
                    totalPagesRead = totalPagesRead ,
                    totalPages = totalPages
                )
            )
        }else {
            BookProgressData(
                bookId = activeBookId ,
                bookImage = cachedBook.bookImage ,
                bookTitle = cachedBook.bookTitle,
                isUri = cachedBook.isUri ,
                totalPages = totalPages,
                currentChapterTitle = cachedBook.currentChapterTitle ,
                currentChapter = cachedBook.currentChapter
            )
        }

    }

    //repair here//
    private fun List<BookLog>.getMostRecentLog() : BookLog {
        return if(this.isNotEmpty()){
            val latest = this.sortedBy { log ->
                log.startedTime
            }
            latest[0]
        }else {
            BookLog()
        }

    }

    private fun calculateProgress(
        totalPages : Int ,
        totalPagesRead : Int
    ) : Float {
        return (totalPagesRead.toFloat() / totalPages.toFloat()).roundToInt().toFloat()
    }

    private fun List<BookLog>.calculateTotalPagesRead() : Int {
        return if(this.isEmpty()){
            0
        }else {
            this.sumOf { it.pagesRead }
        }
    }

    private fun List<BookLog>.calculateTotalTimeSpent() : Long {
        return if(this.isEmpty()){
            0L
        }else {
            this.sumOf { it.period }
        }
    }

    private suspend fun getBookGoalLogs(id : String) : List<BookLog>{
        return logsRepository.getBookLogs(bookId = id).first()
    }

    private suspend fun getCachedBook(id : String) : BookSave =
        booksRepository.getSingleSavedBook(id)


}