package com.dev.james.booktracker.home.domain.repositories

import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.OverallGoal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.utilities.Resource

interface GoalsRepository {
    suspend fun addOverallGoal( overallGoal: OverallGoal) : Resource<Boolean>
    suspend fun addSpecificGoal( specificGoal: SpecificGoal) : Resource<Boolean>
    suspend fun addBookGoal( bookGoal: BookGoal) : Resource<Boolean>
}