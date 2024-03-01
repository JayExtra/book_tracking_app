package com.dev.james.domain.usecases

import com.dev.james.booktracker.core.common_models.SuggestedBook
import com.dev.james.booktracker.core.common_models.mappers.mapToPresentation
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.repository.home.BooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class FetchSuggestedBooksUsecase @Inject constructor(
    private val booksRepository: BooksRepository
) {

    companion object {
        const val TAG = "FetchSuggestedBooksUsecase"
    }

    suspend operator fun invoke() : Resource<List<SuggestedBook>> {
        //this is a place holder, will be getting the most cached category from
        //records and using it here

        Timber.tag(TAG).d("invoked!")
        val category = "Fiction"

        when(val result = booksRepository.getBookByCategory(category)){
            is Resource.Success -> {
               val list =  result.data?.map { bookItem ->
                   bookItem.mapToPresentation()
                }?.filter { book ->
                   book.category.contains(category)
                } ?: listOf(SuggestedBook())
                Timber.tag(TAG).d("Suggested books => ${list.toString()}")
                return Resource.Success( data = list)
            }
            is Resource.Error -> {
                Timber.tag(TAG).d("error => ${result.message}")
                return Resource.Error(message = result.message ?: "Error when fetching suggested books")
            }
            else -> {
                Timber.tag(TAG).d("error => ${result.message}")
                return Resource.Loading(data = emptyList())
            }
        }
    }
}