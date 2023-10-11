package com.dev.james.data.repository

import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.common_models.mappers.mapToEntityObject
import com.dev.james.booktracker.core.common_models.mappers.toDomain
import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.repository.home.GoalsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGoalsRepository : GoalsRepository {

    private val fakeGoalsDatabase = mutableListOf<GoalEntity>()
    override suspend fun saveGoals(goal: Goal): Resource<Boolean> {
        fakeGoalsDatabase.add(goal.mapToEntityObject())
        return Resource.Success(true)
    }

    override fun getAllGoals(): Flow<List<Goal>> {
        return flow { emit(fakeGoalsDatabase.map { it.toDomain() }) }
    }

    override suspend fun deleteGoal(id: String): Resource<Boolean> {
        fakeGoalsDatabase.removeIf { goalEntity ->
            goalEntity.id == id
        }

        return if(fakeGoalsDatabase.none { it.id == id }){
            Resource.Success(true)
        }else{
            Resource.Error(data = false , message = "could not delete the goal")
        }
    }

    override suspend fun getAGoal(id: String): Resource<Goal> {
        val goalEntity = fakeGoalsDatabase.first { it.id == id }
        return if(goalEntity.id.isBlank()){
            Resource.Error(message = "Could not find specified goal")
        }else {
            Resource.Success(goalEntity.toDomain())
        }
    }

}