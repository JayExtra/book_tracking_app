package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core_database.room.entities.BookGoalLogsEntity
import com.dev.james.booktracker.core_database.room.entities.BookGoalsEntity

fun BookGoalLogsEntity.toDomain() : BookGoalLog =
    BookGoalLog(
        bookId, logId, startedTime, endTime, period, pagesRead , currentChapterTitle, currentChapter
    )

fun BookGoalsEntity.toDomain() : BookGoal =
    BookGoal(
        bookId, isChapterGoal, goalInfo, isTimeGoal, goalSet, goalPeriod, specificDays
    )