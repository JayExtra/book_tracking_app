package com.dev.james.booktracker.core.common_models.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.dev.james.booktracker.core.common_models.BookLog
import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.common_models.GoalLog
import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAmount
import kotlin.time.DurationUnit

fun Goal.mapToEntityObject() : GoalEntity {
    return GoalEntity(
        id = goalId ,
        information = goalInfo ,
        time = goalTime ,
        period = goalPeriod ,
        selectedDays = specificDays ,
        shouldShowAlert = shouldShowAlert ,
        booksToRead = booksToRead ,
        booksRead = booksRead ,
        alertNote = alertNote ,
        alertTime = alertTime
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun BookLog.toEntity() : BookLogsEntity {
   return  BookLogsEntity(
        bookId = bookId ,
        logId = logId ,
        startedTime = startedTime ?: LocalDate.now() ,
        endTime = endTime ?: LocalDate.now().plus(1 , ChronoUnit.MINUTES ) ,
        period = period ,
        pagesRead = pagesRead ,
        currentChapterTitle = currentChapterTitle ,
        currentChapter = currentChapter
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun GoalLog.toEntity() : GoalLogsEntity{
    return GoalLogsEntity(
        parentGoalId = parentGoalId ,
        id = id ,
        startTime = startTime ?: LocalDate.now() ,
        endTime = endTime ?: LocalDate.now().plus(1 , ChronoUnit.MINUTES ),
        duration = duration
    )
}

