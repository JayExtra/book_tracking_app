package com.dev.james.data.repository

import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.test_commons.getTestBookGoalEntity
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.repository.home.GoalsRepository

class FakeGoalsRepository : GoalsRepository {
    override suspend fun saveGoals(
        goal: Goal,
        specificGoal: SpecificGoal,
        bookGoal: BookGoal
    ): Resource<Boolean> {
        return Resource.Success(data = true)
    }

    override suspend fun getAllActiveBookGoals(): List<BookGoal> {
        return listOf<BookGoal>(getTestBookGoalEntity().toDomain())
    }
}