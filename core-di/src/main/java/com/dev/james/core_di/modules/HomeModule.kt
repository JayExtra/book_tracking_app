package com.dev.james.core_di.modules


import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_database.room.dao.LogsDao
import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.domain.utilities.ConnectivityManager
import com.dev.james.data.datasources.books.BooksApiDataSourceImpl
import com.dev.james.data.datasources.books.BooksLocalDataSourceImpl
import com.dev.james.data.datasources.goals.GoalsLocalDataSourceImpl
import com.dev.james.data.datasources.logs.LogsLocalDataSourceImpl
import com.dev.james.data.repositories.books.BooksRepositoryImpl
import com.dev.james.data.repositories.goals.GoalsRepositoryImpl
import com.dev.james.data.repositories.logs.LogsRepositoryImpl
import com.dev.james.domain.datasources.home.BooksApiDataSource
import com.dev.james.domain.datasources.home.BooksLocalDataSource
import com.dev.james.domain.datasources.home.GoalsLocalDataSource
import com.dev.james.domain.datasources.home.LogsLocalDataSource
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.home.GoalsRepository
import com.dev.james.domain.repository.home.LogsRepository

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
    fun provideLogsLocalDataSource(
        logsDao: LogsDao
    ) : LogsLocalDataSource {
        return LogsLocalDataSourceImpl(
            logsDao
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
            booksApiDataSource,
            booksLocalDataSource,
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

    @Provides
    @Singleton
    fun provideLogsRepository(
        logsLocalDataSource: LogsLocalDataSource
    ) : LogsRepository {
        return LogsRepositoryImpl(
            logsLocalDataSource
        )
    }
}