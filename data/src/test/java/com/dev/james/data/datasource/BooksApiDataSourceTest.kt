package com.dev.james.data.datasource

import com.dev.james.booktracker.core_network.api_service.BooksApi
import com.dev.james.data.datasources.books.BooksApiDataSourceImpl
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit


class BooksApiDataSourceTest {
    private lateinit var googleBooksApiService : BooksApi
    private lateinit var server : MockWebServer
    private lateinit var booksApiDataSourceImpl: BooksApiDataSourceImpl
    @Before
    fun setUp(){
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        server = MockWebServer()
        googleBooksApiService = Retrofit.Builder()
            .baseUrl(server.url(""))
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BooksApi::class.java)

        booksApiDataSourceImpl =
            BooksApiDataSourceImpl(booksApi = googleBooksApiService)
    }

    private fun enqueueMockResponse(fileName : String){
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream(fileName)
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()
            mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(source.readString(Charsets.UTF_8))
            server.enqueue(mockResponse)
        }
    }

    @After
    fun tearDown(){
        server.shutdown()
    }

    @Test
    fun `querying_for_books_with_correct_parameters_returns_bookVolume_object`() = runTest{
        //given
        enqueueMockResponse("BookVolumesResponse.json")
        //when
        val response = booksApiDataSourceImpl.getQueriedBook(
            query = "ThinkBig" ,
            author = ""
        )
        //then
        assertThat(response.items).isNotEmpty()
    }
}