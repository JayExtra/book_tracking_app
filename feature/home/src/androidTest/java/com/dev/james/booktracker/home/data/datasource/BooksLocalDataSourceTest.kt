package com.dev.james.booktracker.home.data.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dev.james.booktracker.core_database.room.dao.BooksDao
import com.dev.james.booktracker.core_database.room.database.BookTrackerDatabase
import com.dev.james.booktracker.core_database.room.entities.BookEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class BooksLocalDataSourceTest {
    private lateinit var bookTrackerDatabase: BookTrackerDatabase
    private lateinit var booksDao: BooksDao
    private lateinit var booksLocalDataSourceImpl: BooksLocalDataSourceImpl

    val bookEntity = BookEntity(
        bookId = "abab12345",
        bookImage = "somebookimageurl",
        bookTitle = "Some book title" ,
        isUri = false ,
        bookAuthors = "Author1 , Author2" ,
        bookSmallThumbnail = "Somesmallthumbnail",
        bookPagesCount = 122 ,
        publisher = "somepublisher",
        publishedDate = "12/10/2023",
        currentChapter = 11 ,
        chapters = 22 ,
        currentChapterTitle = "Some chapter title"
    )
    @Before
    fun setUp(){
        bookTrackerDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext() ,
            BookTrackerDatabase::class.java
        ).allowMainThreadQueries().build()

        booksDao = bookTrackerDatabase.getBooksDao()
        val dispatcher = StandardTestDispatcher(
            name = "my_test_dispatcher"
        )
        booksLocalDataSourceImpl = BooksLocalDataSourceImpl(
            booksDao = booksDao ,
            dispatcher = dispatcher
        )
    }

    @Test
    fun insertBook_expectedSingleBookInserted() = runTest{
        //when
       val bookAdded =  booksLocalDataSourceImpl.addBookToDataBase(
            bookEntity = bookEntity,
            onBookAdded = { status , _ ->
                status
            }
        )
        //then
        assertThat(bookAdded).isTrue()
    }

    @Test
    fun deleteBook_expectedNoBookInDatabase() = runTest {
        booksLocalDataSourceImpl.addBookToDataBase(
            bookEntity = bookEntity ,
            onBookAdded = { status , _ ->
                status
            }
        )

        val bookDeleted = booksLocalDataSourceImpl.deleteBookFromDataBase(
            bookEntity.bookId ,
            onBookDeleted = { status ->
                status
            }
        )

        assertThat(bookDeleted).isTrue()
    }

    @Test
    fun getCachedBook_returnsListOfBookEntity() = runTest {
         booksLocalDataSourceImpl.addBookToDataBase(
            bookEntity = bookEntity,
            onBookAdded = { status , _ ->
                status
            }
        )
        val addedBooks = booksLocalDataSourceImpl.getAllBooks().first()
        assertThat(addedBooks).isNotEmpty()
    }

    @After
    fun tearDown() {
        bookTrackerDatabase.close()
    }
}