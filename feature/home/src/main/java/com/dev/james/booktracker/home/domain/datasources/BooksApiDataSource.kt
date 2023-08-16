package com.dev.james.booktracker.home.domain.datasources

import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core_network.dtos.BookVolumeDto
import kotlinx.coroutines.flow.Flow

interface BooksApiDataSource {
    suspend fun getQueriedBook(
        query : String ,
        author : String
    ) : BookVolumeDto
}