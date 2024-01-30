package com.dev.james.domain.usecases

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.utilities.formatToDateString
import com.dev.james.booktracker.core.utilities.getDateRange
import com.dev.james.booktracker.core.utilities.getDayString
import com.dev.james.booktracker.core.utilities.getWeekRange
import com.dev.james.booktracker.core.utilities.toAppropriateDay
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.home.LogsRepository
import timber.log.Timber
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
                // this situation happens if you launch the app for the very first time
                //however this will not work well if user already previously used the app , will replace this
                // with api call to fetch last book being read.
                Timber.tag(TAG).d("book id is null or empty not fetching")
                BookProgressData()
            }else {
                Timber.tag(TAG).d("book id is not null or empty , fetching book with id : $requiredBookId!")
                fetchBookProgressData(requiredBookId)
            }

    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun fetchBookProgressData(requiredBookId : String) : BookProgressData {
        val cachedBook = getCachedBook(requiredBookId)
        val bookLogs = getBookGoalLogs(requiredBookId)
        val totalPages = cachedBook.bookPagesCount
        val mappedLogs = mapDataToGraphData(bookLogs)

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
                logs = mappedLogs ,
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
                currentChapterTitle = "",
                currentChapter = 0 ,
                logs = mappedLogs
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mapDataToGraphData(goalLogs : List<BookLog>) : Map<String , Long> {
        val weekRange = getWeekRange()
        val mappedLogs = mutableMapOf<String , Long>()
        val finalLog = mutableMapOf<String , Long>()

        return if (goalLogs.isNotEmpty()){
            goalLogs.forEach { log ->
                log.startedTime?.formatToDateString()?.let { mappedLogs[it] = log.period }
            }
            weekRange.forEach { date ->
                if(mappedLogs.containsKey(date)){
                    mappedLogs[date]?.let { finalLog.put( date.getDayString().toAppropriateDay() , it) }
                }else{
                    finalLog[date.getDayString().toAppropriateDay()] = 0L
                }
            }
            finalLog
        }else {
            weekRange.forEach { date ->
                finalLog[date.getDayString().toAppropriateDay()] = 0L
            }
            finalLog
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
        return logsRepository.getBookLogs(bookId = id , startDate = dateRange.startDate , endDate = dateRange.endDate)
    }

    private suspend fun getCachedBook(id : String) : BookSave =
        booksRepository.getSingleSavedBook(id)

}

