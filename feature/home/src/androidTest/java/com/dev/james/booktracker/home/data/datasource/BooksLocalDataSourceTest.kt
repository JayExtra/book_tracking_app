package com.dev.james.booktracker.home.data.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dev.james.booktracker.core.test_commons.getTestBookEntity
import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.booktracker.core_database.room.entities.BookEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class BooksLocalDataSourceTest {
    private lateinit var bookTrackerDatabase: BookTrackerDatabase
    private lateinit var booksDao: BooksDao
    private lateinit var booksLocalDataSourceImpl: BooksLocalDataSourceImpl
    private lateinit var testScope : TestScope

    @Before
    fun setUp(){
        bookTrackerDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext() ,
            BookTrackerDatabase::class.java
        ).allowMainThreadQueries().build()

        booksDao = bookTrackerDatabase.getBooksDao()
        val dispatcher = StandardTestDispatcher(
            name = "book_tracker_test_dispatcher"
        )
        testScope = TestScope(dispatcher)

        booksLocalDataSourceImpl = BooksLocalDataSourceImpl(
            booksDao = booksDao ,
            dispatcher = dispatcher
        )
    }

    @Test
    fun insertBook_expectedSingleBookInserted() = testScope.runTest {
        //given
        val bookEntity = getTestBookEntity()
        //when
        booksLocalDataSourceImpl.addBookToDataBase(
            bookEntity = bookEntity,
            onBookAdded = { status , _ ->
                status
            }
        )
        val addedBook = booksDao.getBook(bookEntity.bookId)
        //then
        assertThat(addedBook.bookId).isEqualTo(bookEntity.bookId)
    }

    @Test
    fun deleteBook_expectedNoBookInDatabase() = testScope.runTest {
        //given
        val bookEntity = getTestBookEntity()

        //when
        booksLocalDataSourceImpl.addBookToDataBase(
            bookEntity = bookEntity ,
            onBookAdded = { status , _ ->
                status
            }
        )

        booksLocalDataSourceImpl.deleteBookFromDataBase(
            bookEntity.bookId ,
            onBookDeleted = { status ->
                status
            }
        )

        val findDeletedBookResult = booksDao.getBook(bookEntity.bookId)

        assertThat(findDeletedBookResult).isNull()

    }

    @Test
    fun getCachedBook_returnsListOfBookEntity() = testScope.runTest {
        //given
        val bookEntity = getTestBookEntity()

        //when
         booksLocalDataSourceImpl.addBookToDataBase(
            bookEntity = bookEntity,
            onBookAdded = { status , _ ->
                status
            }
        )

        val addedBooks = booksLocalDataSourceImpl.getAllBooks().first()

        //then
        assertThat(addedBooks).isNotEmpty()
    }

    @After
    fun tearDown() {
        bookTrackerDatabase.close()
    }
}