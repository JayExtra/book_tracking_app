package com.dev.james.booktracker.core_database.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.james.booktracker.core_database.room.converters.ListTypeConverter
import com.dev.james.booktracker.core_database.room.dao.OnBoardingDao
import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity

@Database(
    entities = [UserDetailsEntity::class] ,
    version = 1 ,
    exportSchema = false
)
@TypeConverters(ListTypeConverter::class)
abstract class BookTrackerDatabase : RoomDatabase() {
    abstract fun getOnBoardingDao() : OnBoardingDao
}