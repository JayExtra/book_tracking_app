package com.dev.james.my_library.presentation.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.mappers.mapToPresentation
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dev.james.domain.usecases.GetBooksAndProgressUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyBookShelfViewModel @Inject constructor(
    private val getBooksAndProgressUsecase: GetBooksAndProgressUsecase,
    private val booksRepository: BooksRepository
) : ViewModel() {
    private val _screenState : MutableStateFlow<BookShelfUIState> = MutableStateFlow(
        BookShelfUIState()
    )
    val screenState get() = _screenState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.N)
    fun onAction(action : BookShelfUIActions) = viewModelScope.launch {
        when(action){


            is BookShelfUIActions.FetchData -> {
                    _screenState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

               getBooksAndProgressUsecase.invoke().collectLatest { bookData ->
                    val currentlyReading = bookData.filter {
                        it.progress in 1..99
                    }
                    val toRead = bookData.filter {
                        it.progress == 0
                    }
                    val finishedReading = bookData.filter {
                        it.progress == 100
                    }
                    _screenState.update {
                        it.copy(
                            isLoading = false ,
                            currentlyReading = currentlyReading ,
                            notRead = toRead ,
                            finishedReading = finishedReading
                        )
                    }

                }

                booksRepository.getWishList().collectLatest { wishList ->
                        _screenState.update {
                            it.copy(
                                wishList = wishList.map{ bookInfo ->
                                    bookInfo.mapToPresentation()
                                }
                            )
                        }
                }

            }

        }
    }
    sealed class BookShelfUIActions {
        object FetchData : BookShelfUIActions()
    }


}
data class BookShelfUIState(
    val isLoading : Boolean = false ,
    val currentlyReading : List<LibraryBookData> = emptyList(),
    val notRead : List<LibraryBookData> = emptyList(),
    val wishList : List<LibraryBookData> = emptyList(),
    val finishedReading : List<LibraryBookData> = emptyList()
)

