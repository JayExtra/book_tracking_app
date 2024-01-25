package com.dev.james.domain.usecases

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.utilities.formatDateToString
import com.dev.james.booktracker.core.utilities.getDateRange
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.home.LogsRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.time.LocalDate
import java.time.Year
import javax.inject.Inject
import kotlin.math.roundToInt

class FetchActiveBookProgress @Inject constructor(
    private val booksRepository: BooksRepository,
    private val logsRepository: LogsRepository,
    private val dataSoreManager : DataStoreManager
) {
    companion object {
        const val TAG = "FetchActiveBookProgress"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend operator fun invoke(
        bookId : String?
    ) : BookProgressData {

        Timber.tag(TAG).d("invoke triggered!")
            val requiredBookId = bookId ?: dataSoreManager.readStringValueOnce(DataStorePreferenceKeys.CURRENT_ACTIVE_BOOK_ID)

            return if(requiredBookId.trim().isEmpty()){
                Timber.tag(TAG).d("book id is null or empty not fetching")
                BookProgressData()
            }else {
                Timber.tag(TAG).d("book id is not null or empty , fetching book with id : $requiredBookId!")
                fetchBookProgressData(requiredBookId)
            }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun fetchBookProgressData(requiredBookId : String) : BookProgressData {
        val cachedBook = getCachedBook(requiredBookId)
        val bookLogs = getBookGoalLogs(requiredBookId)
        val totalPages = cachedBook.bookPagesCount

        return if (bookLogs.isNotEmpty()){

            val totalPagesRead = bookLogs.calculateTotalPagesRead()
            val totalTimeSpent = bookLogs.calculateTotalTimeSpent()
            val mostRecentLog = bookLogs.getMostRecentLog()

            BookProgressData(
                bookId = requiredBookId ,
                bookImage = cachedBook.bookImage ,
                bookTitle = cachedBook.bookTitle,
                authors = cachedBook.bookAuthors,
                isUri = cachedBook.isUri ,
                totalPages = totalPages,
                totalTimeSpent = totalTimeSpent ,
                totalPagesRead = totalPagesRead ,
                currentChapterTitle = if(mostRecentLog.logId.isNotBlank()) mostRecentLog.currentChapterTitle else "",
                currentChapter = if(mostRecentLog.logId.isNotBlank()) mostRecentLog.currentChapter else 0,
                logs = bookLogs ,
                progress = calculateProgress(
                    totalPagesRead = totalPagesRead ,
                    totalPages = totalPages
                )
            )
        }else {
            BookProgressData(
                bookId = requiredBookId ,
                bookImage = cachedBook.bookImage ,
                bookTitle = cachedBook.bookTitle,
                isUri = cachedBook.isUri ,
                totalPages = totalPages,
                currentChapterTitle = "" ,
                currentChapter = 0
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

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun getBookGoalLogs(id : String) : List<BookLog>{
        val dateRange = getDateRange()
        Timber.tag(TAG).d("date range: $dateRange")
        return logsRepository.getBookLogs(bookId = id , mondayDate = dateRange.startDate , sundayDate = dateRange.endDate)
    }

    private suspend fun getCachedBook(id : String) : BookSave =
        booksRepository.getSingleSavedBook(id)

}

