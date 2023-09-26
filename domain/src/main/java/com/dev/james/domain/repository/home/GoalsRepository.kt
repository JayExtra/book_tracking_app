package com.dev.james.domain.repository.home

import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.BookGoalLog
import com.dev.james.booktracker.core.common_models.OverallGoal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface GoalsRepository {

    suspend fun saveGoals(
        overallGoal: OverallGoal ,
        specificGoal: SpecificGoal ,
        bookGoal: BookGoal
    ) : Resource<Boolean>

    suspend fun getAllActiveBookGoals() : List<BookGoal>

}