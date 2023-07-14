package com.dev.james.booktracker.home.di

import com.dev.james.booktracker.core.utilities.ConnectivityManager
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.booktracker.home.data.datasource.BooksApiDataSource
import com.dev.james.booktracker.home.data.datasource.BooksApiDataSourceImpl
import com.dev.james.booktracker.home.data.repository.BooksRemoteRepository
import com.dev.james.booktracker.home.data.repository.BooksRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideBooksApiDataSource(
        api : BooksApi
    ) : BooksApiDataSource {
        return BooksApiDataSourceImpl(
            api
        )
    }

    @Provides
    @Singleton
    fun provideBooksAndGoalsDAO(
        db : BookTrackerDatabase
    ) = db.getReadAndGoalsDao()

    @Provides
    @Singleton
    fun provideBooksRemoteRepository(
        booksApiDataSource: BooksApiDataSource ,
        connectivityManager: ConnectivityManager
    ) : BooksRemoteRepository {
        return BooksRemoteRepositoryImpl(
            booksApiDataSource ,
            connectivityManager
        )
    }
}