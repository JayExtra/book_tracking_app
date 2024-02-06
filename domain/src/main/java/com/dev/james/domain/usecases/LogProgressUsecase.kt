package com.dev.james.domain.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.entities.updates.BookLogUpdate
import com.dev.james.booktracker.core.entities.updates.GoalLogUpdate
import com.dev.james.booktracker.core.utilities.formatToDateString
import com.dev.james.booktracker.core.utilities.generateSecureUUID
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_datastore.local.datastore.DataStorePreferenceKeys
import com.dev.james.domain.repository.home.LogsRepository
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class LogProgressUsecase @Inject constructor(
    private val logsRepository: LogsRepository,
    private val fetchOrSetStreak: FetchOrSetStreak ,
    private val dataStoreManager: DataStoreManager
) {

    companion object {
        const val TAG = "LogProgressUsecase"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun logCurrentProgress(
        bookId: String,
        readingPeriod: Long,
        currentChapterTitle: String,
        currentPage: Int,
        chapterNumber: Int,
        onSaveComplete: (Boolean) -> Unit
    ) {
        //check for existing logs first
        val currentGoalId =
            dataStoreManager.readStringValueOnce(DataStorePreferenceKeys.CURRENT_ACTIVE_GOAL_ID)
        val mostRecentBookLog = logsRepository.getRecentBookLog(bookId)
        val mostRecentGoalLog = logsRepository.getRecentGoalLog(currentGoalId)
        val currentDate = LocalDate.now()

        Timber.tag(TAG).d("logCurrent Progress triggered!")

        Timber.tag(TAG).d("currentGoalId => $currentGoalId ")
        Timber.tag(TAG).d("mostRecentBooking => $mostRecentBookLog ")
        Timber.tag(TAG).d("mostRecentGoalLog => $mostRecentGoalLog ")
        Timber.tag(TAG).d("currentDate => $currentDate ")

        if (mostRecentBookLog.logId.isNotEmpty()) {
            Timber.tag(TAG).d("book log id not empty!")
            if (mostRecentBookLog.startedTime?.formatToDateString() == currentDate.formatToDateString()) {
                Timber.tag(TAG).d("book log for today exists!")
                //a log for this book exists today
                logsRepository.updateBookLog(
                    BookLogUpdate(
                        logId = mostRecentBookLog.logId,
                        period = mostRecentBookLog.period + readingPeriod,
                        pagesRead = currentPage,
                        currentChapterTitle = currentChapterTitle,
                        currentChapter = chapterNumber,
                        currentPage = currentPage
                    )
                )

                //log the goal as well
                logGoalProgress(
                    parentGoalId = currentGoalId,
                    recentLog = mostRecentGoalLog,
                    currentDate = currentDate,
                    duration = readingPeriod
                )

                onSaveComplete(true)

            } else {

                Timber.tag(TAG).d("book log for today does not exist!")

                // a log for this book doesn't exist for today

                logsRepository.addBookLog(
                    BookLog(
                        bookId = bookId,
                        logId = generateSecureUUID(),
                        startedTime = currentDate,
                        period = readingPeriod,
                        pagesRead = currentPage,
                        currentChapterTitle = currentChapterTitle,
                        currentPage = currentPage,
                        currentChapter = chapterNumber
                    )
                )

                logGoalProgress(
                    parentGoalId = currentGoalId,
                    recentLog = mostRecentGoalLog,
                    currentDate = currentDate,
                    duration = readingPeriod
                )

                fetchOrSetStreak.setStreak()

                onSaveComplete(true)

            }

        } else {
            // a log for this book doesn't exist at all make a new log
            val bookLog =  BookLog(
                bookId = bookId,
                logId = generateSecureUUID(),
                startedTime = currentDate,
                period = readingPeriod,
                pagesRead = currentPage,
                currentChapterTitle = currentChapterTitle,
                currentPage = currentPage,
                currentChapter = chapterNumber
            )

            Timber.tag(TAG).d("no logs at all , making new log => $bookLog")
            logsRepository.addBookLog(
               bookLog
            )

            logGoalProgress(
                parentGoalId = currentGoalId,
                recentLog = mostRecentGoalLog,
                currentDate = currentDate,
                duration = readingPeriod
            )

            fetchOrSetStreak.setStreak()

            onSaveComplete(true)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun logGoalProgress(
        parentGoalId: String,
        recentLog: GoalLog,
        currentDate: LocalDate,
        duration: Long
    ) {
        if (recentLog.id.isNotEmpty()) {
            if (recentLog.startTime?.formatToDateString() == currentDate.formatToDateString()) {
                // we have logged today so update the most recent log
                logsRepository.updateGoalLog(
                    GoalLogUpdate(
                        id = recentLog.id,
                        duration = recentLog.duration + duration
                    )
                )
            } else {
                // we haven't logged any progress today, log
                logsRepository.addGoalLog(
                    GoalLog(
                        parentGoalId = parentGoalId,
                        id = generateSecureUUID(),
                        startTime = currentDate,
                        duration = duration
                    )
                )

            }
        } else {
            //we have no logs currently available in record
            logsRepository.addGoalLog(
                GoalLog(
                    parentGoalId = parentGoalId,
                    id = generateSecureUUID(),
                    startTime = currentDate,
                    duration = duration
                )
            )
        }
    }

}