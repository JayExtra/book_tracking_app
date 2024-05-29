package com.dev.james.my_library.presentation.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.AddBookViewModel
import com.dev.james.booktracker.compose_ui.ui.states.GoogleBottomSheetUiState
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.mappers.mapToDomainObject
import com.dev.james.booktracker.core.common_models.mappers.mapToLibraryData
import com.dev.james.booktracker.core.common_models.mappers.mapToPresentation
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.utilities.convertToOrganisedString
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dev.james.domain.usecases.GetBooksAndProgressUsecase
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyBookShelfViewModel @Inject constructor(
    private val getBooksAndProgressUsecase: GetBooksAndProgressUsecase,
    private val bookDataUsecase : FetchActiveBookProgress ,
    private val booksRepository: BooksRepository ,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val TAG = "MyBookShelfViewModel"
        const val SEARCH_QUERY = "search_query"
        const val NETWORK_SEARCH_QUERY = "network_search_query"
    }
    private val _screenState : MutableStateFlow<BookShelfUIState> = MutableStateFlow(
        BookShelfUIState()
    )
    val screenState get() = _screenState.asStateFlow()

    private val  _screenEvents : Channel<BookShelfUiEvents> = Channel<BookShelfUiEvents>()
    val screenEvents get() = _screenEvents.receiveAsFlow()

    private val searchQueryStateFlow = savedStateHandle.getStateFlow(
        SEARCH_QUERY ,
        ""
    )

    private val networkSearchQueryStateFlow = savedStateHandle.getStateFlow(NETWORK_SEARCH_QUERY ,
        "Think Big"
    )

    private var networkSearchQueryJob : Job? = null

    var bookOptionsDialogState : MutableState<Boolean> = mutableStateOf(false)
        private set


    val bottomSheetSearchFieldState = FormState(
        fields = listOf(
            TextFieldState(
                name = "search_field",
                initial = ""
            )
        )
    )

    private var _googleBottomSheetSearchState: MutableStateFlow<GoogleBottomSheetUiState> =
        MutableStateFlow(GoogleBottomSheetUiState.StandbyState)
    val googleBottomSheetSearchState get() = _googleBottomSheetSearchState.asStateFlow()



    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
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

            is BookShelfUIActions.OnSearch -> {
                _screenState.update {
                    it.copy(
                        isSearching = true ,
                        isSearchLoading = true ,
                        searchEmptyMessage = "" ,
                        searchResult = emptyList()
                    )
                }
                savedStateHandle[SEARCH_QUERY] = action.queryString
                searchQueryStateFlow
                    .debounce(500)
                    .filter { query ->
                        if(query.isEmpty()){
                            _screenState.update { state ->
                                state.copy(
                                    isSearching = false ,
                                    isSearchLoading = false,
                                    searchEmptyMessage = ""
                                )
                            }
                            return@filter false
                        }else{
                            return@filter true
                        }
                    }
                    .distinctUntilChanged()
                    .flatMapLatest { query ->
                        booksRepository.searchBook(query)
                    }.flowOn(Dispatchers.Default)
                    .collectLatest { cachedBooks ->
                        _screenState.update { state ->
                            state.copy(
                               isSearchLoading = false
                            )
                        }
                        if(cachedBooks.isEmpty()){
                            _screenState.update { state ->
                                state.copy(
                                    searchEmptyMessage = "No book found with that particular title"
                                )
                            }
                        }else {
                            _screenState.update { state ->
                                state.copy(
                                    searchEmptyMessage = ""
                                )
                            }
                            val booksList = mutableListOf<LibraryBookData>()
                            cachedBooks.forEach {
                                val bookWithStatsDiffered = async { bookDataUsecase.invoke(it.bookId).mapToLibraryData() }
                                booksList.add(bookWithStatsDiffered.await())
                            }
                            Timber.tag(TAG).d("search result=> $booksList")
                            _screenState.update {
                                it.copy(
                                    searchResult = booksList
                                )
                            }

                        }

                    }
            }

            is BookShelfUIActions.OnBookSelected -> {
                addToRemoveFromWishList(action.book.mapToPresentation())
            }

            is BookShelfUIActions.OnSaveBook -> {
                saveBookToDatabase(
                    action.baseBookData ,
                    action.chapters
                )
            }

            is BookShelfUIActions.OnRemoveFromWishList-> {
                addToRemoveFromWishList(action.baseBookData)
            }

            is BookShelfUIActions.OnNetworkSearch -> {
                networkSearchBook(action.query)
            }

            is BookShelfUIActions.ShowBookOptionDialog -> {
                bookOptionsDialogState.value = true
            }

            is BookShelfUIActions.HideBookOptionDialog -> {
                bookOptionsDialogState.value = false
            }

            is BookShelfUIActions.HideBookAddedDialog -> {
                _screenEvents.send(
                    BookShelfUiEvents.HideBookAddedDialog
                )
            }

            is BookShelfUIActions.HideRemovedFromWishlistDialog -> {
                _screenEvents.send(
                    BookShelfUiEvents.HideRemoveFromWishList
                )
            }

            is BookShelfUIActions.HideBookSavedToDbDialog -> {
                _screenEvents.send(
                    BookShelfUiEvents.HideBookSavedToDbDialog
                )
            }

            else -> {}
        }


    }

    fun cancelQuery(){
        networkSearchQueryJob?.cancel()
    }

    private fun saveBookToDatabase(
        baseBookData: BaseBookData ,
        chapters : Int
    ) = viewModelScope.launch {
        val bookSave = BookSave(
            bookId = baseBookData.id ,
            bookImage = baseBookData.image ,
            bookTitle = baseBookData.title ,
            bookAuthors = baseBookData.authors ,
            bookDescription = baseBookData.description ,
            bookSmallThumbnail = "",
            bookPagesCount = baseBookData.pages ,
            publisher = baseBookData.publisher ,
            publishedDate = baseBookData.publishedDate ,
            isUri = false ,
            category = baseBookData.category.convertToOrganisedString() ,
            chapters = chapters
        )
        val result = booksRepository.saveBookToDatabase(bookSave)
        if(result){
            Timber.tag(MyLibraryViewModel.TAG).d("Book saved to database.")
            _screenEvents.send(
               BookShelfUiEvents
                    .ShowBookAddedToDBDialog
            )
            booksRepository.addOrRemoveFromWishList(baseBookData)
        }else {
            Timber.tag(MyLibraryViewModel.TAG).d("Could not save to db.")
        }
    }

    private fun addToRemoveFromWishList(baseBookData: BaseBookData) = viewModelScope.launch {
        val result = booksRepository.addOrRemoveFromWishList(baseBookData).split(":")[0]
        when(result){
            "added" -> {
               _screenEvents.send(
                   BookShelfUiEvents.ShowBookAddedDialog(baseBookData)
               )
            }
            "removed" -> {
                _screenEvents.send(
                    BookShelfUiEvents.ShowRemoveFromWishListDialog
                )
            }
            else -> {}
        }
        Timber.tag(MyLibraryViewModel.TAG).d(result)
    }


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun networkSearchBook(searchQuery: String) {
        networkSearchQueryJob = viewModelScope.launch {
            savedStateHandle[NETWORK_SEARCH_QUERY] = searchQuery
            networkSearchQueryStateFlow
                .debounce(200)
                .filter { query ->
                    if (query.isEmpty()) {
                        val searchViewState =
                            bottomSheetSearchFieldState.getState<TextFieldState>("search_field")
                        searchViewState.change("")
                        _googleBottomSheetSearchState.update {
                            GoogleBottomSheetUiState.StandbyState
                        }
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    booksRepository.getBooksFromApi(bookTitle = query, bookAuthor = "")
                }
                .flowOn(Dispatchers.Default)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {

                            val booksList = resource.data?.items

                            if (!booksList.isNullOrEmpty()) {
                                _googleBottomSheetSearchState.update {
                                    GoogleBottomSheetUiState.HasFetched(
                                        booksList = booksList.map { bookDto -> bookDto.mapToDomainObject() }
                                    )
                                }

                            } else {

                                _googleBottomSheetSearchState.update {
                                    GoogleBottomSheetUiState
                                        .HasFetched(
                                            booksList = emptyList()
                                        )
                                }
                            }
                            Timber.tag(AddBookViewModel.TAG).d(booksList.toString())

                        }

                        is Resource.Error -> {
                            val errorMessage = resource.message ?: "Oops! Something went wrong"

                            _googleBottomSheetSearchState.update {
                                GoogleBottomSheetUiState.HasFailed(
                                    errorMessage = errorMessage
                                )
                            }

                        }

                        is Resource.Loading -> {
                            _googleBottomSheetSearchState.update {
                                GoogleBottomSheetUiState.IsLoading
                            }
                        }

                        else -> {}
                    }

                }

        }
    }
    sealed class BookShelfUIActions {
        object FetchData : BookShelfUIActions()
        object NavigateBack : BookShelfUIActions()

        data class OnSearch(val queryString:String) : BookShelfUIActions()

        data class OnNetworkSearch(val query : String) : BookShelfUIActions()

        data class OnBookSelected(val book : Book) : BookShelfUIActions()

        data class OnSaveBook(val baseBookData: BaseBookData,val chapters: Int) : BookShelfUIActions()

        data class OnRemoveFromWishList(val baseBookData: BaseBookData) : BookShelfUIActions()

        data class OpenBookDetailsScreen(val baseBookData: BaseBookData) : BookShelfUIActions()

        data class ShowBookDetailsDialog(val baseBookData: BaseBookData) : BookShelfUIActions()

        object CloseBookDetailsDialog : BookShelfUIActions()

        object ShowBookOptionDialog : BookShelfUIActions()

        object HideBookOptionDialog : BookShelfUIActions()

        object HideBookAddedDialog : BookShelfUIActions()

        object HideRemovedFromWishlistDialog : BookShelfUIActions()

        object HideBookSavedToDbDialog : BookShelfUIActions()


        
    }

    sealed class BookShelfUiEvents {
        object Default : BookShelfUiEvents()
        object NavigateBack : BookShelfUiEvents()

        data class NavigateToBookDetailsScreen(val baseBookData: BaseBookData) : BookShelfUiEvents()

        data class ShowBookDetailsDialog(val baseBookData: BaseBookData) : BookShelfUiEvents()

        data class ShowBookAddedDialog(val book : BaseBookData) : BookShelfUiEvents()

        object HideBookAddedDialog : BookShelfUiEvents()

        object CloseBookDetailsDialog : BookShelfUiEvents()

        object ShowRemoveFromWishListDialog : BookShelfUiEvents()

        object HideRemoveFromWishList : BookShelfUiEvents()

        object ShowBookAddedToDBDialog : BookShelfUiEvents()

        object HideBookSavedToDbDialog : BookShelfUiEvents()


    }


}
data class BookShelfUIState(
    val isLoading : Boolean = false,
    val isSearching : Boolean = false,
    val isSearchLoading : Boolean = false,
    val searchResult : List<LibraryBookData> = emptyList(),
    val searchEmptyMessage : String = "",
    val currentlyReading : List<LibraryBookData> = emptyList(),
    val notRead : List<LibraryBookData> = emptyList(),
    val wishList : List<BaseBookData> = emptyList(),
    val finishedReading : List<LibraryBookData> = emptyList(),
)

