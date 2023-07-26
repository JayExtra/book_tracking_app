package com.dev.james.booktracker.core_network.api_service

import com.dev.james.booktracker.core_network.dtos.BookVolumeDto
import com.dev.james.booktracker.core_network.utilities.Endpoints.VOLUME_ENDPOINT
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {
    @GET(VOLUME_ENDPOINT)
    suspend fun queryForBook(
        @Query("q") query : String,
        @Query("inauthor") author : String
    ) : BookVolumeDto

}