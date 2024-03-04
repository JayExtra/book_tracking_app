package com.dev.james.domain.usecases

import com.dev.james.booktracker.core.common_models.SuggestedBook
import com.dev.james.booktracker.core.common_models.mappers.mapToPresentation
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.main.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class FetchSuggestedBooksUsecase @Inject constructor(
    private val booksRepository: BooksRepository ,
    private val repository: UserPreferencesRepository
) {

    companion object {
        const val TAG = "FetchSuggestedBooksUsecase"
    }

    suspend operator fun invoke(): Resource<List<SuggestedBook>> {

        val frequentCategory = getFrequentCategory() ?: getDefaultCategory()

        Timber.tag(TAG).d("invoked!")

        when (val result = booksRepository.getBookByCategory(frequentCategory)) {
            is Resource.Success -> {
                val list = result.data?.map { bookItem ->
                    bookItem.mapToPresentation()
                }?.filter { book ->
                    book.category.contains(frequentCategory)
                } ?: listOf(SuggestedBook())
                Timber.tag(TAG).d("Suggested books => ${list.toString()}")
                return Resource.Success(data = list)
            }

            is Resource.Error -> {
                Timber.tag(TAG).d("error => ${result.message}")
                return Resource.Error(
                    message = result.message ?: "Error when fetching suggested books"
                )
            }

            else -> {
                Timber.tag(TAG).d("error => ${result.message}")
                return Resource.Loading(data = emptyList())
            }
        }
    }

    private suspend fun getFrequentCategory(): String? {

        val cachedBooks = booksRepository.getSavedBooks().first()
        if (cachedBooks.isEmpty()) {
            return null
        }
        val categoriesInDb = cachedBooks.map { book ->
            book.category
        }
        val cleanedCategories = categoriesInDb
            .toString()
            .drop(1)
            .dropLast(1)
            .replace("\\s".toRegex(), "")
            .split(",")

        return cleanedCategories.groupingBy {
            it
        }.eachCount()
            .maxByOrNull {
                it.value
            }?.key
    }

    private suspend fun getDefaultCategory() : String {
        val userDetails = repository.getUserDetails().first()
        return userDetails.favouriteGenres[0]
    }


}