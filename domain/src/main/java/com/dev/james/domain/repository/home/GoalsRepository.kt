package com.dev.james.domain.repository.home

import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface GoalsRepository {

    suspend fun saveGoals(
        goal: Goal
    ) : Resource<Boolean>

    fun getActiveGoals() : Flow<List<Goal>>

    suspend fun deleteGoal(id : String) : Resource<Boolean>

    suspend fun getAGoal(id : String) : Resource<Goal>


}