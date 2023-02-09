package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OnBoardingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserData(user : UserDetailsEntity)

    @Query("SELECT * FROM user_details")
    fun getUserData() : Flow<List<UserDetailsEntity>>
}