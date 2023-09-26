package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.james.booktracker.core.entities.BookGoalsEntity
import com.dev.james.booktracker.core.entities.OverallGoalEntity
import com.dev.james.booktracker.core.entities.SpecificGoalsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalsDao {

    /*goals db functions called here*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOverallGoal(overallGoalEntity: OverallGoalEntity)

    @Query("SELECT * FROM overall_goals_table WHERE id =:goalId")
    suspend fun getOverallGoal(goalId : String) : OverallGoalEntity


    @Query("SELECT * FROM overall_goals_table")
    fun getAllOverallGoals() : Flow<List<OverallGoalEntity>>

    @Query("DELETE FROM overall_goals_table WHERE id =:goalId")
    suspend fun deleteOverallGoal(goalId : String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpecificGoal(specificGoalsEntity: com.dev.james.booktracker.core.entities.SpecificGoalsEntity)

    @Query("SELECT * FROM specific_goals_table WHERE goal_id =:goalId")
    suspend fun getSingleSpecificGoal(goalId : String) : SpecificGoalsEntity


    @Query("SELECT * FROM specific_goals_table")
    fun getAllSpecificGoals() : Flow<List<SpecificGoalsEntity>>

    @Query("DELETE FROM specific_goals_table WHERE goal_id =:goalId")
    suspend fun deleteSpecificGoal(goalId : String)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookGoal(bookGoalsEntity: BookGoalsEntity)

    @Query("SELECT * FROM book_goal_table WHERE book_id =:bookId")
    suspend fun getBookGoal(bookId : String) : BookGoalsEntity

    @Query("SELECT * FROM book_goal_table")
    fun getAllBookGoals() : Flow<List<BookGoalsEntity>>

    @Query("DELETE FROM book_goal_table WHERE book_id =:bookId")
    suspend fun deleteBookGoal(bookId : String)

}