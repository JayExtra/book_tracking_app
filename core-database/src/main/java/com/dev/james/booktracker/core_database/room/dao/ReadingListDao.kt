package com.dev.james.booktracker.core_database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.james.booktracker.core.entities.ReadingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingListDao {
    @Query("SELECT * FROM reading_lists ORDER BY date_created DESC")
    fun getReadingLists() : Flow<List<ReadingListEntity>>

    @Query("SELECT * FROM reading_lists WHERE id=:id")
    fun getAReadingList(id : String) : ReadingListEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReadingList(readingListEntity: ReadingListEntity)

    @Query("DELETE FROM reading_lists WHERE id=:id ")
    suspend fun deleteReadingList(id : String)

}