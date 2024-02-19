package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.entities.updates.GoalUpdate
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalsDao {

    /*goals db functions called here*/
    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOverallGoal(goal: GoalEntity)
*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGoal(goal : GoalEntity)

    @Query("SELECT * FROM goals_table WHERE id =:goalId")
    suspend fun getGoalById(goalId : String) : GoalEntity


    @Query("SELECT * FROM goals_table")
    fun getAllGoals() : Flow<List<GoalEntity>>

    /*@Query("DELETE FROM goals_table WHERE id =:goalId")
    suspend fun deleteOverallGoal(goalId : String)
*/
    @Query("DELETE FROM goals_table WHERE id =:goalId")
    suspend fun deleteGoal(goalId: String)

    @Update(entity = GoalEntity::class)
    suspend fun updateBooksRead(goalUpdate: GoalUpdate)


}