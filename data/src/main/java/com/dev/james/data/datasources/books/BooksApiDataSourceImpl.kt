package com.dev.james.data.datasources.books


import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.booktracker.core.dto.BookVolumeDto
import com.dev.james.domain.datasources.home.BooksApiDataSource
import javax.inject.Inject

class BooksApiDataSourceImpl @Inject constructor(
    private val booksApi: BooksApi 
) : BooksApiDataSource {

    override suspend fun getQueriedBook(query: String, author: String): BookVolumeDto {
        return booksApi.queryForBook(
            query = query ,
            author = author
        )
    }

    override suspend fun queryByCategory(query: String): BookVolumeDto {
        return booksApi.queryByCategory(
            query = query ,
            maxResult = 30
        )
    }
}