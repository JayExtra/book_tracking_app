package com.dev.james.booktracker.core_network.api_service

import com.dev.james.booktracker.core_network.utilities.Endpoints.BOOKS_ENDPOINT
import retrofit2.http.GET

interface BooksApi {
    @GET(BOOKS_ENDPOINT)
    fun getSomething()
}
