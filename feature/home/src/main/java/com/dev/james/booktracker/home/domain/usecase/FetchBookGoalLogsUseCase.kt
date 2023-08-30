package com.dev.james.booktracker.home.domain.usecase

import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.BookGoalData
import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.home.domain.repositories.BooksRepository
import com.dev.james.booktracker.home.domain.repositories.GoalsRepository
import com.dev.james.booktracker.home.domain.repositories.LogsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
            BookGoalData(
                bookId = bookId ,
                bookImage = cachedBook.bookImage ,
                isUri = cachedBook.isUri ,
                totalPages = cachedBook.bookPagesCount,
                totalTimeSpent = bookGoalLogs.calculateTotalTimeSpent() ,
                totalPagesRead = bookGoalLogs.calculateTotalPagesRead() ,
                logs = bookGoalLogs
            )
        }else{
            BookGoalData()
        }
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