package com.dev.james.booktracker.home.domain.usecase

import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.BookGoalData
import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.home.domain.repositories.BooksRepository
import com.dev.james.booktracker.home.domain.repositories.GoalsRepository
import com.dev.james.booktracker.home.domain.repositories.LogsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.roundToInt

class FetchBookGoalLogsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository ,
    private val booksRepository: BooksRepository ,
    private val logsRepository: LogsRepository
) {

    suspend operator fun invoke() : BookGoalData {
        val activeGoal = getActiveBookGoal()
        return if(activeGoal.isNotEmpty()){
            val bookId = activeGoal[0].bookId
            val bookGoalLogs = getBookGoalLogs(bookId)
            val cachedBook = getCachedBook(bookId)
            val totalPagesRead = bookGoalLogs.calculateTotalPagesRead()
            val totalTimeSpent = bookGoalLogs.calculateTotalTimeSpent()
            val totalPages = cachedBook.bookPagesCount
            val mostRecentLog = bookGoalLogs.getMostRecentLog()

            BookGoalData(
                bookId = bookId ,
                bookImage = cachedBook.bookImage ,
                bookTitle = cachedBook.bookTitle,
                isUri = cachedBook.isUri ,
                totalPages = totalPages,
                totalTimeSpent = totalTimeSpent ,
                totalPagesRead = totalPagesRead ,
                currentChapterTitle = if(mostRecentLog.logId.isNotBlank()) mostRecentLog.currentChapterTitle else cachedBook.currentChapterTitle ,
                currentChapter = if(mostRecentLog.logId.isNotBlank()) mostRecentLog.currentChapter else cachedBook.currentChapter,
                logs = bookGoalLogs ,
                progress = calculateProgress(
                    totalPagesRead = totalPagesRead ,
                    totalPages = totalPages
                )
            )
        }else{
            BookGoalData()
        }
    }

    //repair here//
    private fun List<BookGoalLog>.getMostRecentLog() : BookGoalLog {
        return if(this.isNotEmpty()){
            val latest = this.sortedBy { log ->
                log.startedTime
            }
            latest[0]
        }else {
            BookGoalLog()
        }

    }

    private fun calculateProgress(
        totalPages : Int ,
        totalPagesRead : Int
    ) : Float {
        return (totalPagesRead.toFloat() / totalPages.toFloat()).roundToInt().toFloat()
    }

    private fun List<BookGoalLog>.calculateTotalPagesRead() : Int {
        return if(this.isEmpty()){
            0
        }else {
            this.sumOf { it.pagesRead }
        }
    }

    private fun List<BookGoalLog>.calculateTotalTimeSpent() : Long {
        return if(this.isEmpty()){
            0L
        }else {
            this.sumOf { it.period }
        }
    }

    private suspend fun getBookGoalLogs(id : String) : List<BookGoalLog>{
        return logsRepository.getBookGoalLogs(id = id).first()
    }

    private suspend fun getActiveBookGoal() : List<BookGoal> {
        return goalsRepository.getAllActiveBookGoals()
    }

    private suspend fun getCachedBook(id : String) : BookSave =
        booksRepository.getSingleSavedBook(id)


}