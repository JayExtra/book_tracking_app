package com.dev.james.my_library.presentation.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.mappers.mapToPresentation
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dev.james.domain.usecases.GetBooksAndProgressUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyBookShelfViewModel @Inject constructor(
    private val getBooksAndProgressUsecase: GetBooksAndProgressUsecase,
    private val booksRepository: BooksRepository
) : ViewModel() {

    companion object {
        const val TAG = "MyBookShelfViewModel"
    }
    private val _screenState : MutableStateFlow<BookShelfUIState> = MutableStateFlow(
        BookShelfUIState()
    )
    val screenState get() = _screenState.asStateFlow()

    private val  _screenEvents : Channel<BookShelfUiEvents> = Channel<BookShelfUiEvents>()
    val screenEvents get() = _screenEvents.receiveAsFlow()

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
                   Timber.tag(TAG).d("My saved books => ${bookData.toString()}")
                    val currentlyReading = bookData.filter {
                        it.progress in 1..99
                    }
                    val toRead = bookData.filter {
                        it.progress == 0
                    }
                    val finishedReading = bookData.filter {
                        it.progress == 100
                    }

                   booksRepository.getWishList().collectLatest { wishList ->
                       Timber.tag(TAG).d("My wishlist => ${wishList.toString()}")
                       _screenState.update {
                           it.copy(
                               isLoading = false ,
                               currentlyReading = currentlyReading ,
                               notRead = toRead ,
                               finishedReading = finishedReading ,
                               wishList = wishList
                           )
                       }
                   }
                }



            }
            is BookShelfUIActions.NavigateBack -> {
                    _screenEvents.send(
                        BookShelfUiEvents.NavigateBack
                    )
            }

            is BookShelfUIActions.OpenBookDetailsScreen -> {
                _screenEvents.send(
                    BookShelfUiEvents.NavigateToBookDetailsScreen(action.baseBookData)
                )
            }

            is BookShelfUIActions.ShowBookDetailsDialog -> {
                _screenEvents.send(
                    BookShelfUiEvents.ShowBookDetailsDialog(action.baseBookData)
                )
            }
            
            is BookShelfUIActions.CloseBookDetailsDialog -> {
                _screenEvents.send(
                    BookShelfUiEvents.CloseBookDetailsDialog
                )
            }

            else -> {}
        }
    }
    sealed class BookShelfUIActions {
        object FetchData : BookShelfUIActions()
        object NavigateBack : BookShelfUIActions()

        data class OpenBookDetailsScreen(val baseBookData: BaseBookData) : BookShelfUIActions()

        data class ShowBookDetailsDialog(val baseBookData: BaseBookData) : BookShelfUIActions()
        object CloseBookDetailsDialog : BookShelfUIActions()
        
    }

    sealed class BookShelfUiEvents {
        object Default : BookShelfUiEvents()
        object NavigateBack : BookShelfUiEvents()

        data class NavigateToBookDetailsScreen(val baseBookData: BaseBookData) : BookShelfUiEvents()

        data class ShowBookDetailsDialog(val baseBookData: BaseBookData) : BookShelfUiEvents()
        object CloseBookDetailsDialog : BookShelfUiEvents()


    }


}
data class BookShelfUIState(
    val isLoading : Boolean = false,
    val currentlyReading : List<LibraryBookData> = emptyList(),
    val notRead : List<LibraryBookData> = emptyList(),
    val wishList : List<BaseBookData> = emptyList(),
    val finishedReading : List<LibraryBookData> = emptyList()
)

