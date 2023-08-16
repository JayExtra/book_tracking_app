package com.dev.james.booktracker.home.domain.repositories

import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.OverallGoal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.utilities.Resource

interface GoalsRepository {

    suspend fun saveGoals(
        overallGoal: OverallGoal ,
        specificGoal: SpecificGoal ,
        bookGoal: BookGoal
    ) : Resource<Boolean>

}