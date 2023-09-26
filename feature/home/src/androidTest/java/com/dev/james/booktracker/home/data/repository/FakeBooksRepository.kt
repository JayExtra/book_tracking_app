package com.dev.james.booktracker.home.data.repository

import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.test_commons.getFakeBookSaveWithUrl
import com.dev.james.booktracker.core.test_commons.getTestBookVolumeDto
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.dto.BookVolumeDto
import com.dev.james.data.local.abst.repositories.BooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBooksRepository : com.dev.james.data.local.abst.repositories.BooksRepository {
    override fun getBooksFromApi(
        bookTitle: String,
        bookAuthor: String
    ): Flow<Resource<BookVolumeDto>> = flow {
        emit(
            Resource.Success(
                data = getTestBookVolumeDto()
            )
        )
    }

    override suspend fun saveBookToDatabase(bookSave: BookSave): Boolean {
        return true
    }

    override suspend fun deleteBookInDatabase(bookId: String): Boolean {
        return true
    }

    override fun getSavedBooks(): Flow<List<BookSave>> = flow {
        emit(
            listOf(getFakeBookSaveWithUrl())
        )
    }

    override suspend fun getSingleSavedBook(id: String): BookSave {
        return getFakeBookSaveWithUrl()
    }
}