package com.dev.james.booktracker.home.di

import com.dev.james.booktracker.core.utilities.ConnectivityManager
import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.booktracker.home.domain.datasources.BooksApiDataSource
import com.dev.james.booktracker.home.data.datasource.BooksApiDataSourceImpl
import com.dev.james.booktracker.home.domain.datasources.BooksLocalDataSource
import com.dev.james.booktracker.home.data.datasource.BooksLocalDataSourceImpl
import com.dev.james.booktracker.home.data.datasource.GoalsLocalDataSourceImpl
import com.dev.james.booktracker.home.domain.repositories.BooksRepository
import com.dev.james.booktracker.home.data.repository.BooksRepositoryImpl
import com.dev.james.booktracker.home.data.repository.GoalsRepositoryImpl
import com.dev.james.booktracker.home.domain.datasources.GoalsLocalDataSource
import com.dev.james.booktracker.home.domain.repositories.GoalsRepository
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
    fun provideGoalsLocalDataSource(
        goalsDao: GoalsDao
    ) : GoalsLocalDataSource {
        return GoalsLocalDataSourceImpl(
            goalsDao
        )
    }


    @Provides
    @Singleton
    fun provideBooksRemoteRepository(
        booksApiDataSource: BooksApiDataSource,
        booksLocalDataSource: BooksLocalDataSource,
        connectivityManager: ConnectivityManager
    ) : BooksRepository {
        return BooksRepositoryImpl(
            booksApiDataSource ,
            booksLocalDataSource ,
            connectivityManager
        )
    }

    @Provides
    @Singleton
    fun provideGoalsRepository(
        goalsLocalDataSource : GoalsLocalDataSource
    ) : GoalsRepository {
        return GoalsRepositoryImpl(
            goalsLocalDataSource
        )
    }




}