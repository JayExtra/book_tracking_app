package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoreDao {
    @Query("SELECT * FROM user_details")
    fun getUserInformation() : Flow<List<UserDetailsEntity>>
}