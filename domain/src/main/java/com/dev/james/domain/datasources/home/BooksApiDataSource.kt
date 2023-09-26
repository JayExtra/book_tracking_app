package com.dev.james.domain.datasources.home

import com.dev.james.booktracker.core.dto.BookVolumeDto

interface BooksApiDataSource {
    suspend fun getQueriedBook(
        query : String ,
        author : String
    ) : BookVolumeDto
}