package com.dev.james.booktracker.home.data.repository

import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core_network.dtos.BookVolumeDto
import kotlinx.coroutines.flow.Flow

interface BooksRemoteRepository {
    suspend fun getBooksFromApi(
        bookTitle : String ,
        bookAuthor : String
    ) : Flow<Resource<BookVolumeDto>>
}