package com.dev.james.core_di.modules


import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_database.room.dao.LogsDao
import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.domain.utilities.ConnectivityManager
import com.dev.james.data.datasources.home.BooksApiDataSourceImpl
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
        return com.dev.james.data.datasources.home.BooksLocalDataSourceImpl(
            booksDao = booksDao
        )
    }

    @Provides
    @Singleton
    fun provideGoalsLocalDataSource(
        goalsDao: GoalsDao
    ) : GoalsLocalDataSource {
        return com.dev.james.data.datasources.home.GoalsLocalDataSourceImpl(
            goalsDao
        )
    }

    @Provides
    @Singleton
    fun provideLogsLocalDataSource(
        logsDao: LogsDao
    ) : LogsLocalDataSource {
        return com.dev.james.data.datasources.home.LogsLocalDataSourceImpl(
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
        return com.dev.james.data.repositories.home.BooksRepositoryImpl(
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
        return com.dev.james.data.repositories.home.GoalsRepositoryImpl(
            goalsLocalDataSource
        )
    }

    @Provides
    @Singleton
    fun provideLogsRepository(
        logsLocalDataSource: LogsLocalDataSource
    ) : LogsRepository {
        return com.dev.james.data.repositories.home.LogsRepositoryImpl(
            logsLocalDataSource
        )
    }
}