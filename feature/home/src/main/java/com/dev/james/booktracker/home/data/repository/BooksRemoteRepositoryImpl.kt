package com.dev.james.booktracker.home.data.repository

import coil.network.HttpException
import com.dev.james.booktracker.core.utilities.ConnectivityManager
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core_network.dtos.BookVolumeDto
import com.dev.james.booktracker.home.data.datasource.BooksApiDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BooksRemoteRepositoryImpl
    @Inject constructor(
        private val booksApiDataSource: BooksApiDataSource ,
        private val connectivityManager: ConnectivityManager
    ): BooksRemoteRepository {

    override suspend fun getBooksFromApi(bookTitle: String, bookAuthor: String): Flow<Resource<BookVolumeDto>> = flow {
        emit(Resource.Loading())
        if(connectivityManager.getNetworkStatus()){
            try{
                val booksFromApi = booksApiDataSource.getQueriedBook(query = bookTitle , author = bookAuthor)
                emit(Resource.Success(data = booksFromApi))
            }catch (e : HttpException){
                emit(Resource.Error(
                    message = e.message ?: "Oops , seems the problem is in our side. Please be patient as we try to fix."
                ))
            }
        }else{
            emit(Resource.Error(
                message = "Oops , seems the problem is in your side. Please reconnect your device network and try again."
            ))
        }
    }
}