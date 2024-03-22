package com.dev.james.booktracker.core_database.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.james.booktracker.core_database.room.converters.DateTypeConverter
import com.dev.james.booktracker.core_database.room.converters.ListTypeConverter
import com.dev.james.booktracker.core_database.room.dao.CoreDao
import com.dev.james.booktracker.core.entities.UserDetailsEntity
import com.dev.james.booktracker.core_database.room.dao.OnBoardingDao
import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_database.room.dao.LogsDao
import com.dev.james.booktracker.core.entities.BookEntity
import com.dev.james.booktracker.core.entities.BookLogsEntity
import com.dev.james.booktracker.core.entities.GoalEntity
import com.dev.james.booktracker.core.entities.GoalLogsEntity
import com.dev.james.booktracker.core.entities.ReadingListEntity
import com.dev.james.booktracker.core_database.room.dao.ReadingListDao

@Database(
    entities = [UserDetailsEntity::class , BookEntity::class , BookLogsEntity::class ,
    GoalEntity::class , GoalLogsEntity::class , ReadingListEntity::class ] ,
    version = 12 ,
    exportSchema = false
)
@TypeConverters(ListTypeConverter::class , DateTypeConverter::class)
abstract class BookTrackerDatabase : RoomDatabase() {
    abstract fun getOnBoardingDao() : OnBoardingDao
    abstract fun getCoreDao() : CoreDao

    abstract fun getBooksDao() : BooksDao

    abstract fun getGoalsDao() : GoalsDao

    abstract fun getLogsDao() : LogsDao

    abstract fun getReadingListDao() : ReadingListDao
}