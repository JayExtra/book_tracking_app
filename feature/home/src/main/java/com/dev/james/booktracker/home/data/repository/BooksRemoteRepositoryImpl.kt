package com.dev.james.booktracker.home.data.repository

import coil.network.HttpException
import com.dev.james.booktracker.core.utilities.ConnectivityManager
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core_network.dtos.BookVolumeDto
import com.dev.james.booktracker.home.data.datasource.BooksApiDataSource
import javax.inject.Inject

class BooksRemoteRepositoryImpl
    @Inject constructor(
        private val booksApiDataSource: BooksApiDataSource
        ): BooksRemoteRepository {

    override suspend fun getBooksFromApi(bookTitle: String, bookAuthor: String): Resource<BookVolumeDto> {
        return try{
            val booksFromApi = booksApiDataSource.getQueriedBook(query = bookTitle , author = bookAuthor)
            Resource.Success(data = booksFromApi)
        }catch (e : HttpException){
            Resource.Error(
                message = e.message ?: "Oops , seems the problem is in our side. Please be patient as we try to fix."
            )
        }
    }
}