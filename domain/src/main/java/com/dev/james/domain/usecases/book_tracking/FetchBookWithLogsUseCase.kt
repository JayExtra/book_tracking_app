package com.dev.james.domain.usecases.book_tracking

import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.common_models.BookStatsData
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.home.LogsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.roundToInt

class FetchBookWithLogsUseCase @Inject constructor(
    private val booksRepository: BooksRepository ,
    private val logsRepository : LogsRepository
) {

    suspend operator fun invoke(bookId: String): BookStatsData {
        val book = booksRepository.getSingleSavedBook(bookId)
        val logs = logsRepository.getBookGoalLogs(bookId).first()
        val totalPagesRead = logs.calculateTotalPagesRead()
        val totalTimeSpent = logs.calculateTotalTimeSpent()
        val currentChapter = logs.getMostRecentLog().currentChapter
        val chapterTitle = logs.getMostRecentLog().currentChapterTitle
        val progress = calculateProgress(
            totalPages = book.bookPagesCount,
            totalPagesRead = totalPagesRead
        )

        return BookStatsData(
            bookId = bookId,
            bookImage = book.bookImage,
            bookTitle = book.bookTitle,
            author = book.bookAuthors,
            isUri = book.isUri,
            totalPages = book.bookPagesCount,
            totalTimeSpent = totalTimeSpent,
            totalPagesRead = totalPagesRead,
             progress = if(progress.toInt() == 0) 0.05f else progress ,
            currentChapter = if(currentChapter == 0) book.currentChapter else currentChapter ,
            currentChapterTitle = chapterTitle.ifBlank { book.currentChapterTitle }
        )
    }

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
}