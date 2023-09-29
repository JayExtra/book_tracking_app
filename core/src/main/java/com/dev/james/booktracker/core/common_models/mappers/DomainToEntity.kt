package com.dev.james.booktracker.core.common_models.mappers

import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.OverallGoal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.entities.BookGoalsEntity
import com.dev.james.booktracker.core.entities.OverallGoalEntity
import com.dev.james.booktracker.core.entities.SpecificGoalsEntity

fun OverallGoal.mapToEntityObject() : OverallGoalEntity {
    return OverallGoalEntity(
        goalId = goalId ,
        goalInfo = goalInfo ,
        goalTime = goalTime ,
        goalPeriod = goalPeriod ,
        specificDays = specificDays ,
        shouldShowAlert = shouldShowAlert ,
        alertNote = alertNote ,
        alertTime = alertTime
    )
}

fun BookGoal.mapToEntityObject() : BookGoalsEntity {
    return BookGoalsEntity(
        bookId = bookId ,
        goalInfo = goalInfo ,
        isChapterGoal = isChapterGoal ,
        isTimeGoal = isTimeGoal ,
        goalSet = goalSet ,
        goalPeriod = goalPeriod ,
        specificDays = specificDays
    )
}

fun SpecificGoal.mapToEntityObject() : SpecificGoalsEntity {
    return SpecificGoalsEntity(
        goalId = goalId ,
        bookCountGoal = bookCount ,
        booksReadCount = booksReadCount
    )
}
