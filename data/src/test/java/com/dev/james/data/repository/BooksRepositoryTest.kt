package com.dev.james.data.repository

import com.dev.james.booktracker.core.test_commons.getTestBookEntity
import com.dev.james.booktracker.core.test_commons.getTestBookVolumeDto
import com.dev.james.domain.utilities.ConnectivityManager
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.data.repositories.books.BooksRepositoryImpl
import com.dev.james.domain.datasources.home.BooksApiDataSource
import com.dev.james.domain.datasources.home.BooksLocalDataSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.given
import retrofit2.HttpException
import retrofit2.Response

class BooksRepositoryTest {

    private lateinit var testScope : TestScope
    private lateinit var booksApiDataSource: BooksApiDataSource
    private lateinit var booksLocalDataSource: BooksLocalDataSource
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var booksRepositoryImpl: BooksRepositoryImpl

    @Before
    fun setUp(){
        val dispatcher = StandardTestDispatcher(
            name = "book_tracker_test_dispatcher"
        )
        testScope = TestScope(dispatcher)
        booksApiDataSource = Mockito.mock(BooksApiDataSource::class.java)
        booksLocalDataSource = Mockito.mock(BooksLocalDataSource::class.java)
        connectivityManager = Mockito.mock(ConnectivityManager::class.java)

        booksRepositoryImpl = BooksRepositoryImpl(
            booksApiDataSource = booksApiDataSource,
            booksLocalDataSource = booksLocalDataSource,
            connectivityManager = connectivityManager,
            dispatcher = dispatcher
        )
    }

    @Test
    fun getBooksFromGoogleApi_withConnectivity_shouldReturnResourceOfBooksVolume() = testScope.runTest {
        //given
        Mockito.`when`(connectivityManager.getNetworkStatus()).thenReturn(true)
        Mockito.`when`(booksApiDataSource.getQueriedBook("Some title" , "")).thenReturn(
            getTestBookVolumeDto()
        )

        //when
        val apiCallResult = booksRepositoryImpl.getBooksFromApi("Some title" , "").first()

        //then
        when(apiCallResult){
            is Resource.Success -> {
                assertThat(apiCallResult.data?.totalItems).isEqualTo(1)
            }
            else -> {}
        }

    }

    @Test
    fun getBooksFromGoogleApi_withNoConnectivity_shouldReturnErrorMessage() = testScope.runTest {
        //given
        Mockito.`when`(connectivityManager.getNetworkStatus()).thenReturn(false)
        Mockito.`when`(booksApiDataSource.getQueriedBook("Some title" , "")).thenReturn(
            getTestBookVolumeDto()
        )

        //when
        val apiCallResult = booksRepositoryImpl.getBooksFromApi("Some title" , "").first()

        //then
        when(apiCallResult){
            is Resource.Error -> {
                assertThat(apiCallResult.message).contains("your side")
            }
            else -> {}
        }
    }

    @Test
    fun getBooksFromApi_withException429Thrown_shouldReturnErrorMessage() = testScope.runTest {
        Mockito.`when`(connectivityManager.getNetworkStatus()).thenReturn(false)
        given(booksApiDataSource.getQueriedBook("Some title" , "")).willAnswer {
            val response: Response<String> = Response.error(429, "Too many requests".toResponseBody("plain/text".toMediaTypeOrNull()))
            throw HttpException(response)
        }
        val apiCallResult = booksRepositoryImpl.getBooksFromApi("Some title" , "").first()

        //then
        when(apiCallResult){
            is Resource.Error -> {
                assertThat(apiCallResult.message).contains("Too many")
            }
            else -> {}
        }
    }
    @Test
    fun getBooksFromApi_withException500Thrown_shouldReturnErrorMessage() = testScope.runTest {
        Mockito.`when`(connectivityManager.getNetworkStatus()).thenReturn(false)
        given(booksApiDataSource.getQueriedBook("Some title" , "")).willAnswer {
            val response: Response<String> = Response.error(500, "Error reaching server.".toResponseBody("plain/text".toMediaTypeOrNull()))
            throw HttpException(response)
        }
        val apiCallResult = booksRepositoryImpl.getBooksFromApi("Some title" , "").first()

        //then
        when(apiCallResult){
            is Resource.Error -> {
                assertThat(apiCallResult.message).contains("Oops")
            }
            else -> {}
        }
    }

    @Test
    fun getLocallySavedBooks_returnsListOfBookSave() = testScope.runTest {
        Mockito.`when`(booksLocalDataSource.getAllBooks()).thenReturn(
            flow{
              emit(listOf(getTestBookEntity()))
            }
        )

        val bookSaveList = booksRepositoryImpl.getSavedBooks().first()

        assertThat(bookSaveList).isNotEmpty()

    }

}