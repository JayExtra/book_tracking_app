package com.dev.james.booktracker.home.data.datasource


import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.booktracker.core_network.dtos.BookVolumeDto
import com.dev.james.booktracker.home.data.datasource.BooksApiDataSource
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
}