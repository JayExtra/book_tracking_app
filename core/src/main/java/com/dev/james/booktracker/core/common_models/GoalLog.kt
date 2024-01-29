package com.dev.james.booktracker.core.common_models

import java.time.LocalDate

data class GoalLog(
    val parentGoalId : String = "",
    val id : String = "",
    val startTime : LocalDate? = null,
    val endTime : LocalDate? = null,
    val duration : Long = 0L
)
