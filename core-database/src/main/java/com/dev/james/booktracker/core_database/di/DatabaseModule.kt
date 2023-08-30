package com.dev.james.booktracker.core_database.di

import android.content.Context
import androidx.room.Room
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) : BookTrackerDatabase {
        return Room.databaseBuilder(
            context.applicationContext ,
            BookTrackerDatabase::class.java ,
            "book_tracker_database"
        ).fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideBooksDAO(
        db : BookTrackerDatabase
    ) = db.getBooksDao()

    @Provides
    @Singleton
    fun provideGoalsDAO(
        db : BookTrackerDatabase
    ) = db.getGoalsDao()

    @Provides
    @Singleton
    fun provideLogsDAO(
        db : BookTrackerDatabase
    ) = db.getLogsDao()


}