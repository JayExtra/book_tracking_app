package com.dev.james.booktracker.core_database.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.james.booktracker.core_database.room.converters.DateTypeConverter
import com.dev.james.booktracker.core_database.room.converters.ListTypeConverter
import com.dev.james.booktracker.core_database.room.dao.CoreDao
import com.dev.james.booktracker.core_database.room.entities.UserDetailsEntity
import com.dev.james.booktracker.core_database.room.dao.OnBoardingDao
import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_database.room.dao.LogsDao
import com.dev.james.booktracker.core_database.room.entities.BookEntity
import com.dev.james.booktracker.core_database.room.entities.BookGoalLogsEntity
import com.dev.james.booktracker.core_database.room.entities.BookGoalsEntity
import com.dev.james.booktracker.core_database.room.entities.OverallGoalEntity
import com.dev.james.booktracker.core_database.room.entities.OverallGoalLogsEntity
import com.dev.james.booktracker.core_database.room.entities.SpecificGoalsEntity

@Database(
    entities = [UserDetailsEntity::class , BookEntity::class , BookGoalsEntity::class , BookGoalLogsEntity::class ,
    OverallGoalEntity::class , OverallGoalLogsEntity::class , SpecificGoalsEntity::class] ,
    version = 4 ,
    exportSchema = false
)
@TypeConverters(ListTypeConverter::class , DateTypeConverter::class)
abstract class BookTrackerDatabase : RoomDatabase() {
    abstract fun getOnBoardingDao() : OnBoardingDao
    abstract fun getCoreDao() : CoreDao

    abstract fun getBooksDao() : BooksDao

    abstract fun getGoalsDao() : GoalsDao

    abstract fun getLogsDao() : LogsDao
}