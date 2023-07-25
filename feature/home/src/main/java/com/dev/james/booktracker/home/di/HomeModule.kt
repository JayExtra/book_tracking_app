package com.dev.james.booktracker.home.di

import com.dev.james.booktracker.core.utilities.ConnectivityManager
import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.booktracker.home.data.datasource.BooksApiDataSource
import com.dev.james.booktracker.home.data.datasource.BooksApiDataSourceImpl
import com.dev.james.booktracker.home.data.datasource.BooksLocalDataSource
import com.dev.james.booktracker.home.data.datasource.BooksLocalDataSourceImpl
import com.dev.james.booktracker.home.data.repository.BooksRepository
import com.dev.james.booktracker.home.data.repository.BooksRepositoryImpl
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
    fun provideBooksLocalDataSource(
        booksDao: BooksDao
    ) : BooksLocalDataSource {
        return BooksLocalDataSourceImpl(
            booksDao = booksDao
        )
    }

    @Provides
    @Singleton
    fun provideBooksDAO(
        db : BookTrackerDatabase
    ) = db.getBooksDao()

    @Provides
    @Singleton
    fun provideBooksRemoteRepository(
        booksApiDataSource: BooksApiDataSource ,
        booksLocalDataSource: BooksLocalDataSource ,
        connectivityManager: ConnectivityManager
    ) : BooksRepository {
        return BooksRepositoryImpl(
            booksApiDataSource ,
            booksLocalDataSource ,
            connectivityManager
        )
    }




}