package com.dev.james.my_library.presentation.ui.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.AddBookViewModel
import com.dev.james.booktracker.compose_ui.ui.states.GoogleBottomSheetUiState
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.booktracker.core.common_models.mappers.mapToDomainObject
import com.dev.james.booktracker.core.common_models.mappers.mapToLibraryData
import com.dev.james.booktracker.core.common_models.mappers.mapToPresentation
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReadingListScreenViewModel @Inject constructor(
    private val readingListsRepository: ReadingListsRepository,
    private val fetchActiveBookProgress: FetchActiveBookProgress ,
    private val booksRepository: BooksRepository ,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val TAG = "ReadingListScreenViewModel"
    }

    private val _state: MutableStateFlow<ReadingListScreenState> =
        MutableStateFlow(value = ReadingListScreenState())
    val state get() = _state.asStateFlow()

    var deleteConfirmationDialogState by mutableStateOf(value = false)
        private set
    var deleteReadingListDialogState by mutableStateOf(false)
        private set

    var deleteBookConfirmationDialogState by mutableStateOf(false)
        private set

    var bookDeletedDialogState by mutableStateOf(false)
        private set

    var pendingBookDialogState by mutableStateOf(false)
        private set
    var confirmRemovePendingBookDialogState by mutableStateOf(false)
        private set

    var pendingBookRemovedDialogState by mutableStateOf(false)
        private set

    private val searchQueryStateFlow = savedStateHandle.getStateFlow("search_query" ,
        AddBookViewModel.DEFAULT_SEARCH_QUERY
    )

    private var queryJob: Job? = null

    val savedBooks = booksRepository.getSavedBooks()
        .stateIn(
            viewModelScope ,
            started = SharingStarted.WhileSubscribed(5000) ,
            emptyList()
        )


    var selectedBookState by mutableStateOf(Book())
        private set

    var selectedSavedBookState by mutableStateOf(BookSave())
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

    /*var selectedBookState by mutableStateOf(LibraryBookData())
        private set*/

    @SuppressLint("NewApi")
    fun onAction(action: ReadingListScreenAction) = viewModelScope.launch {
        when (action) {
            is ReadingListScreenAction.FetchReadingList -> {
                val id = action.readingListId
                _state.update { screenState ->
                    screenState.copy(
                        isLoading = true,
                        readingListFetchError = "" ,
                        readingList = ReadingLists()
                    )
                }
                fetchReadingListDetails(
                   id
                )
                fetchPendingBooks(
                    id
                )
            }
            is ReadingListScreenAction.DeleteReadingList -> {
                readingListsRepository.deleteReadingList(action.readingListId)
                val readingListIds = readingListsRepository.getReadingLists().first().map { it.id }
                if(!readingListIds.contains(action.readingListId)){
                    deleteReadingListDialogState = false
                    deleteConfirmationDialogState = true
                }
            }
            is ReadingListScreenAction.DeleteBook -> {
               /* selectedBookState = action.book*/
                val result = booksRepository.addOrRemoveBookFromReadingList(
                    bookId = action.book.id ,
                    readingListId = action.readingListId
                )
                when(result){
                    is Resource.Success -> {
                        val message = result.data?.split(":")!!
                        if(message[0] == "removed"){
                            bookDeletedDialogState = true
                            deleteBookConfirmationDialogState = false
                        }
                    }
                    is Resource.Error -> {
                        Timber.tag(TAG).d("ERROR : ${result.message}")
                    }
                    else -> {}
                }
            }
            is ReadingListScreenAction.DismissDialog -> {
                when(action.dialogType){
                     DialogType.READING_LIST_DELETED_DIALOG -> {
                        deleteConfirmationDialogState = false
                     }
                    DialogType.CONFIRM_DELETE_READING_LIST_DIALOG -> {
                        deleteReadingListDialogState = false
                    }
                    DialogType.BOOK_DELETED_DIALOG -> {
                        bookDeletedDialogState = false
                    }
                    DialogType.CONFIRM_BOOK_DELETE_DIALOG -> {
                        deleteBookConfirmationDialogState = false
                    }
                    DialogType.PENDING_BOOK_ADDED_DIALOG -> {
                        pendingBookDialogState = false
                    }
                    DialogType.PENDING_BOOK_REMOVED -> {
                        pendingBookRemovedDialogState = false
                    }
                    else -> {}
                }
            }

            is ReadingListScreenAction.ShowDialog -> {
                when(action.dialogType){
                    DialogType.CONFIRM_DELETE_READING_LIST_DIALOG -> {
                        deleteReadingListDialogState = true
                    }
                    DialogType.CONFIRM_BOOK_DELETE_DIALOG -> {
                        deleteBookConfirmationDialogState = true
                    }
                    DialogType.CONFIRM_REMOVING_PENDING_BOOK -> {
                        confirmRemovePendingBookDialogState = true
                    }
                    else -> {}
                }
            }

            is ReadingListScreenAction.AddBookFromApi -> {
               addToPendingBooksList(
                   readingListId = action.readingListId ,
                   book = action.book.mapToPresentation()
               )
            }
            is ReadingListScreenAction.RemoveBookFromApi -> {
                removeFromPendingBooksList(
                    readingListId = action.readingListId ,
                    bookId = action.bookId
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchReadingListDetails(id: String) = viewModelScope.launch {
        readingListsRepository.getSingleReadingList(id)
            .map { item ->
                val differedBooks = item.readingList.map {
                    async { fetchActiveBookProgress.invoke(it) }
                }
                val booksListWithData = differedBooks.awaitAll().map {
                    it.mapToLibraryData()
                }
                Timber.tag(TAG).e("books with data : $booksListWithData")
                ReadingLists(
                    id = item.id,
                    name = item.name,
                    image = item.image,
                    description = item.description,
                    books = booksListWithData,
                    date = item.date,
                    starred = item.starred
                )
            }
            .catch { t ->
                Timber.tag(TAG).e("Error fetching reading list => ${t.printStackTrace()}")
                _state.update { screenState ->
                    screenState.copy(
                        isLoading = false,
                        readingList = ReadingLists(),
                        readingListFetchError = "Could not fetch reading list details => ${t.localizedMessage}"
                    )
                }
            }
            .collectLatest { readingList ->
                _state.update { screenState ->
                    screenState.copy(
                        isLoading = false,
                        readingList = readingList,
                        readingListFetchError = ""
                    )
                }
            }
    }

    private fun fetchPendingBooks(readingListId: String) = viewModelScope.launch {
        readingListsRepository.getPendingReadingListItem(readingListId)
            .map { readingLists ->
                readingLists.books
            }.catch { t ->
                Timber.tag(TAG).e("Error getting pending books! => ${t.printStackTrace()}")
                _state.update { screenState ->
                    screenState.copy(
                        pendingListFetchError = "Oops there was a problem fetching these books. Please refresh."
                    )
                }
            }.collectLatest { books ->
                _state.update { screenState ->
                    screenState.copy(
                        pendingBooks = books.map { it.mapToPresentation() } ,
                        pendingListFetchError = ""
                    )
                }
            }
    }

    private fun addToPendingBooksList(readingListId: String , book: BaseBookData) = viewModelScope.launch {
        val result = readingListsRepository.addToPendingList(
            readingListId ,
            book
        )
        when(result){
            is Resource.Success -> {
                if(result.data == true){
                    pendingBookDialogState = true
                }else{
                    Timber.tag(TAG).e("Could not add book to pending list")
                }
            }
            is Resource.Error -> {
                Timber.tag(TAG).e("Could not add book to pending list => ${result.message}")
            }
            else -> {}
        }
    }
    private fun removeFromPendingBooksList(readingListId: String , bookId: String) = viewModelScope.launch {
        val result = readingListsRepository.removeFromPendingList(
            readingListId ,
            bookId
        )
        when(result){
            is Resource.Success -> {
                if(result.data == true){
                    confirmRemovePendingBookDialogState = false
                    pendingBookRemovedDialogState = true
                }else{
                    Timber.tag(TAG).e("Could not add book to pending list")
                }
            }
            is Resource.Error -> {
                Timber.tag(TAG).e("Could not add book to pending list => ${result.message}")
            }
            else -> {}
        }
    }



    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun searchForBook(searchQuery: String) {
        queryJob = viewModelScope.launch {
            //searchQueryStateFlow.value = searchQuery
            savedStateHandle["search_query"] = searchQuery
            searchQueryStateFlow
                .debounce(200)
                .filter { query ->
                    if (query.isEmpty()) {
                        val searchViewState =
                            bottomSheetSearchFieldState.getState<TextFieldState>("search_field")
                        searchViewState.change("")
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
                                _googleBottomSheetSearchState.value =
                                    GoogleBottomSheetUiState.HasFetched(
                                        booksList = booksList.map { bookDto -> bookDto.mapToDomainObject() }
                                    )
                            } else {
                                _googleBottomSheetSearchState.value = GoogleBottomSheetUiState
                                    .HasFetched(
                                        booksList = emptyList()
                                    )
                            }
                            Timber.tag(AddBookViewModel.TAG).d(booksList.toString())

                        }

                        is Resource.Error -> {
                            val errorMessage = resource.message ?: "Oops! Something went wrong"
                            _googleBottomSheetSearchState.value =
                                GoogleBottomSheetUiState.HasFailed(
                                    errorMessage = errorMessage
                                )
                        }

                        is Resource.Loading -> {
                            _googleBottomSheetSearchState.value = GoogleBottomSheetUiState.IsLoading
                        }

                        else -> {}
                    }

                }

        }
    }

    override fun onCleared() {
        super.onCleared()
        queryJob?.cancel()
    }

    fun cancelQueryJob() {
        queryJob?.cancel()
    }


    sealed class ReadingListScreenAction {
        data class FetchReadingList(val readingListId: String) : ReadingListScreenAction()
        data class DeleteReadingList(val readingListId :String) : ReadingListScreenAction()

        data class DeleteBook(val book : LibraryBookData , val readingListId: String) : ReadingListScreenAction()

        data class AddBookFromApi(val book : Book , val readingListId: String) : ReadingListScreenAction()

        data class RemoveBookFromApi(val bookId : String , val readingListId: String) : ReadingListScreenAction()

        data class DismissDialog(val dialogType : DialogType) : ReadingListScreenAction()

        data class ShowDialog(val dialogType: DialogType) : ReadingListScreenAction()
    }

    data class ReadingListScreenState(
        val isLoading: Boolean = false,
        val readingList: ReadingLists = ReadingLists(),
        val readingListFetchError: String = "" ,
        val pendingBooks : List<LibraryBookData> = listOf() ,
        val pendingListFetchError : String = ""
    )

    enum class DialogType {
        READING_LIST_DELETED_DIALOG ,
        BOOK_DELETED_DIALOG ,
        PENDING_BOOK_ADDED_DIALOG ,
        PENDING_BOOK_REMOVED ,
        CONFIRM_DELETE_READING_LIST_DIALOG ,
        CONFIRM_BOOK_DELETE_DIALOG ,
        CONFIRM_REMOVING_PENDING_BOOK
    }
}
