package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity

fun BookLogsEntity.toDomain() : BookLog =
    BookLog(
        bookId, logId, startedTime, endTime, period, pagesRead , currentChapterTitle, currentChapter
    )

fun GoalEntity.toDomain() : Goal =
    Goal(
        goalId = id ,
        goalInfo = information,
        goalTime = time ,
        goalPeriod = period ,
        specificDays = selectedDays ,
        shouldShowAlert = shouldShowAlert ,
        booksToRead = booksToRead ,
        booksRead = booksRead ,
        alertNote = alertNote ,
        alertTime = alertTime
    )

fun GoalLogsEntity.toDomain() : GoalLog {
    return GoalLog(
        parentGoalId, id, startTime, endTime, duration
    )
}