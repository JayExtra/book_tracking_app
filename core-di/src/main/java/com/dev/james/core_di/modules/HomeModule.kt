package com.dev.james.core_di.modules


import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.dao.GoalsDao
import com.dev.james.booktracker.core_database.room.dao.LogsDao
import com.dev.james.booktracker.core_database.room.dao.ReadingListDao
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.domain.utilities.ConnectivityManager
import com.dev.james.data.datasources.books.BooksApiDataSourceImpl
import com.dev.james.data.datasources.books.BooksLocalDataSourceImpl
import com.dev.james.data.datasources.goals.GoalsLocalDataSourceImpl
import com.dev.james.data.datasources.logs.LogsLocalDataSourceImpl
import com.dev.james.data.datasources.reading_lists.ReadingListLocalDatasourceImpl
import com.dev.james.data.repositories.reading_lists.ReadingListRepositoryImpl
import com.dev.james.data.repositories.books.BooksRepositoryImpl
import com.dev.james.data.repositories.goals.GoalsRepositoryImpl
import com.dev.james.data.repositories.logs.LogsRepositoryImpl
import com.dev.james.domain.datasources.home.BooksApiDataSource
import com.dev.james.domain.datasources.home.BooksLocalDataSource
import com.dev.james.domain.datasources.home.GoalsLocalDataSource
import com.dev.james.domain.datasources.home.LogsLocalDataSource
import com.dev.james.domain.datasources.reading_lists.ReadingListLocalDatasource
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.home.GoalsRepository
import com.dev.james.domain.repository.home.LogsRepository
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository

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
        dataStoreManager: DataStoreManager ,
        readingListLocalDatasource: ReadingListLocalDatasource ,
        connectivityManager: ConnectivityManager
    ) : BooksRepository {
        return BooksRepositoryImpl(
            booksApiDataSource,
            booksLocalDataSource,
            dataStoreManager ,
            connectivityManager ,
            readingListLocalDatasource
        )
    }

    @Provides
    @Singleton
    fun provideGoalsRepository(
        goalsLocalDataSource : GoalsLocalDataSource ,
        dataStoreManager: DataStoreManager
    ) : GoalsRepository {
        return GoalsRepositoryImpl(
            goalsLocalDataSource ,
            dataStoreManager
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

    @Provides
    @Singleton
    fun provideReadingListLocalDatasource(
        readingListDao: ReadingListDao
    ) : ReadingListLocalDatasource = ReadingListLocalDatasourceImpl(readingListDao)

    @Provides
    @Singleton
    fun provideReadingListRepository(
        readingListLocalDatasource: ReadingListLocalDatasource
    ) : ReadingListsRepository = ReadingListRepositoryImpl(readingListLocalDatasource)
}