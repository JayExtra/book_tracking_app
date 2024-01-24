package com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.mappers.mapToBookUiObject
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.utilities.convertToAuthorsString
import com.dev.james.booktracker.core.utilities.generateSecureUUID
import com.dev.james.domain.repository.home.BooksRepository
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
class AddBookViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
): ViewModel() {

    companion object {
        const val TAG = "AddBookViewModel"
    }

    private var queryJob: Job? = null

    private val _imageSelectorState: MutableStateFlow<ImageSelectorUiState> = MutableStateFlow(
        ImageSelectorUiState()
    )
    val imageSelectorUiState get() = _imageSelectorState.asStateFlow()

    private val _selectedBookState: MutableStateFlow<Book> = MutableStateFlow(
        Book()
    )
    val selectedBookState get() = _selectedBookState

    private val savedBookState: MutableStateFlow<BookSave> = MutableStateFlow(
        BookSave()
    )

    private val searchQueryMutableStateFlow: MutableStateFlow<String> =
        MutableStateFlow("Think Big")

    private var _googleBottomSheetSearchState: MutableStateFlow<GoogleBottomSheetUiState> =
        MutableStateFlow(GoogleBottomSheetUiState.StandbyState)
    val googleBottomSheetSearchState get() = _googleBottomSheetSearchState.asStateFlow()

    private var _addBookScreenUiEvents: Channel<AddBookScreenUiEvents> = Channel()
    val addBookScreenUiEvents get() = _addBookScreenUiEvents.receiveAsFlow()

    var saveProgressBarState by mutableStateOf(false)
        private set


    val currentReadFormState = FormState(
        fields = listOf(
            TextFieldState(
                name = "title",
                validators = listOf(Validators.Required(message = "Book title is required."))
            ),
            TextFieldState(
                name = "author",
                validators = listOf(Validators.Required(message = "An author for the book is needed."))
            ),
            TextFieldState(
                name = "chapters",
                transform = { it.toInt() },
                validators = listOf(Validators.Required(message = "Please specify the number of chapters."))
            ),
            TextFieldState(
                name = "current chapter",
                validators = listOf(Validators.Required(message = "Please specify the current chapter you are in."))
            ),
            TextFieldState(
                name = "chapter title",
                transform = { it.toInt() },
                validators = listOf(Validators.Required(message = "Please specify the current chapter title."))
            ),
            TextFieldState(
                name = "pages_count",
                validators = listOf(Validators.Required(message = "Please specify the number of pages in the book."))
            )
        )
    )

    private val currentReadFormTitleFieldState: TextFieldState =
        currentReadFormState.getState("title")
    private val currentReadFormAuthorFieldState: TextFieldState =
        currentReadFormState.getState("author")
    private val currentReadFormPagesFieldState: TextFieldState =
        currentReadFormState.getState("pages_count")

    private val currentReadFormChaptersState: TextFieldState =
        currentReadFormState.getState("chapters")
    private val currentReadFormCurrentChapter: TextFieldState =
        currentReadFormState.getState("current chapter")
    private val currentReadFormCurChaptTitleState: TextFieldState =
        currentReadFormState.getState("chapter title")

    val bottomSheetSearchFieldState = FormState(
        fields = listOf(
            TextFieldState(
                name = "search_field",
                initial = ""
            )
        )
    )

    private fun validateImageSelected(imageUri: Uri , imageUrl : String) {
        if (imageUri != Uri.EMPTY || imageUrl.isNotBlank()) {
            _imageSelectorState.value = _imageSelectorState.value.copy(
                showProgress = false,
                isError = false
            )
        } else {
            _imageSelectorState.value = _imageSelectorState.value.copy(
                showProgress = false,
                isError = true
            )
        }
    }

    suspend fun passUiAction(action : CurrentReadFormActions ) {

        when (action) {
            is CurrentReadFormActions.LaunchImagePicker -> {
                _imageSelectorState.value = _imageSelectorState.value.copy(
                    showProgress = true
                )
            }

            is CurrentReadFormActions.DismissImagePicker -> {
                _imageSelectorState.value = _imageSelectorState.value.copy(
                    showProgress = false
                )
            }

            is CurrentReadFormActions.ImageSelected -> {

                _imageSelectorState.value = _imageSelectorState.value.copy(
                    showProgress = false,
                    isError = false,
                    imageSelectedUri = action.imageUri
                )

            }

            is CurrentReadFormActions.ClearImage -> {
                _imageSelectorState.value = _imageSelectorState.value.copy(
                    imageSelectedUri = Uri.EMPTY,
                    imageUrl = ""
                )

            }

            is CurrentReadFormActions.SaveBookToDatabase -> {
                //saveBookToDb(action.bookSave)
                //validate the form and the image picker first
               validateImageSelected(
                    imageUri = _imageSelectorState.value.imageSelectedUri ,
                    imageUrl = _imageSelectorState.value.imageUrl
                )
                val formValidationResult = currentReadFormState.validate()
                //check validation result
                if (formValidationResult && !_imageSelectorState.value.isError) {
                    if (currentReadFormCurrentChapter.value.toInt() > currentReadFormChaptersState.value.toInt()) {
                            _addBookScreenUiEvents.send(
                                AddBookScreenUiEvents.ShowSnackBar(
                                    message =  "The current chapter you are in exceeds the total chapters in this book" ,
                                    isSaving = false
                                )
                            )
                    }else {

                      /*  _addBookScreenUiEvents.send(
                            AddBookScreenUiEvents.ShowSaveProgressBar(
                                message = "Saving your book to the database"
                            )
                        )*/
                       saveProgressBarState = true

                        val author = currentReadFormAuthorFieldState.value
                        val title = currentReadFormTitleFieldState.value
                        val pages = currentReadFormPagesFieldState.value
                        val chapters = currentReadFormChaptersState.value
                        val currentChapter = currentReadFormCurrentChapter.value
                        val chapterTitle = currentReadFormCurChaptTitleState.value

                        val bookId = if (_imageSelectorState.value.imageSelectedUri != Uri.EMPTY)
                            generateSecureUUID()
                        else
                            selectedBookState.value.bookId ?: "n/a"


                        val isUri = _imageSelectorState.value.imageSelectedUri != Uri.EMPTY

                        val bookImage =
                            if (isUri) _imageSelectorState.value.imageSelectedUri.toString() else _imageSelectorState.value.imageUrl

                        val publisher = selectedBookState.value.publisher
                        val publishedDate = selectedBookState.value.publishedDate

                        val bookSave = BookSave(
                            bookId = bookId,
                            bookImage = bookImage,
                            bookTitle = title,
                            bookAuthors = author,
                            bookSmallThumbnail = "n/a",
                            bookPagesCount = pages.toInt(),
                            publisher = publisher ?: "n/a",
                            publishedDate = publishedDate ?: "n/a",
                            isUri = isUri,
                            chapters = chapters.toInt(),
                            currentChapter = currentChapter.toInt(),
                            currentChapterTitle = chapterTitle
                        )

                        saveBookToDb(bookSave)
                    }
                }
            }

            is CurrentReadFormActions.UndoBookSaving -> {
                undoBookSaved()
            }

            is CurrentReadFormActions.Navigate -> {
                _addBookScreenUiEvents.send(
                    AddBookScreenUiEvents.Navigate
                )
            }

            is CurrentReadFormActions.CloseCameraScreen -> {
                _imageSelectorState.update { state ->
                    state.copy(showProgress = false)
                }
            }

            else -> {}
        }

    }

    private fun saveBookToDb(bookSave: BookSave) = viewModelScope.launch {
        Timber.tag(TAG).d("Save action triggered")
        if (booksRepository.saveBookToDatabase(bookSave)) {
            savedBookState.value = bookSave
            //show snackbar
            _addBookScreenUiEvents.send(
                AddBookScreenUiEvents.ShowSnackBar(
                    message = "${bookSave.bookTitle} added to library.",
                    isSaving = true
                )
            )
            //for testing, if it does not work please remove
           /* _addBookScreenUiEvents.send(
                AddBookScreenUiEvents.HideSaveProgressBar
            )*/
            saveProgressBarState = false

            Timber.tag(TAG).d("Book successfully added to database")
        } else {
            Timber.tag(TAG).d("Could not add any book to database")
        }

    }

    private fun undoBookSaved() = viewModelScope.launch {
        val result = booksRepository.deleteBookInDatabase(savedBookState.value.bookId)
        Timber.tag(TAG).d("Book id = ${savedBookState.value.bookId}")

        if (result) {
            //dismiss snackbar
            Timber.tag(TAG).d("Book removed from db successfully.")

            _addBookScreenUiEvents.send(
                AddBookScreenUiEvents.ShowSnackBar(
                    message = "${savedBookState.value.bookTitle} removed from library.",
                    isSaving = false
                )
            )
            //reset the book save state
            savedBookState.value = BookSave()
        }
    }

    fun onBookSelected(book: Book) {
        //update various states
        _imageSelectorState.value = imageSelectorUiState.value.copy(
            imageUrl = book.bookImage!!,
            imageSelectedUri = Uri.EMPTY
        )

        currentReadFormTitleFieldState.change(book.bookTitle ?: "No title found")
        currentReadFormAuthorFieldState.change(
            book.bookAuthors?.convertToAuthorsString() ?: "No author(s) found."
        )
        currentReadFormPagesFieldState.change(book.bookPagesCount.toString())
        _selectedBookState.value = book

    }


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun searchForBook(searchQuery: String) {
        queryJob = viewModelScope.launch {
            searchQueryMutableStateFlow.value = searchQuery
            searchQueryMutableStateFlow
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
                                        booksList = booksList.map { bookDto -> bookDto.mapToBookUiObject() }
                                    )
                            } else {
                                _googleBottomSheetSearchState.value = GoogleBottomSheetUiState
                                    .HasFetched(
                                        booksList = emptyList()
                                    )
                            }
                            Timber.tag(TAG).d(booksList.toString())

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

    fun cancelQueryJob() {
        queryJob?.cancel()
    }

}

sealed class CurrentReadFormActions {
    object SaveBookToDatabase : CurrentReadFormActions()

    object UndoBookSaving : CurrentReadFormActions()
    object LaunchImagePicker : CurrentReadFormActions()
    data class ImageSelected(val imageUri: Uri) : CurrentReadFormActions()
    object DismissImagePicker : CurrentReadFormActions()
    object ClearImage : CurrentReadFormActions()

    object Navigate : CurrentReadFormActions()

    object CloseCameraScreen : CurrentReadFormActions()

}

sealed class AddBookScreenUiEvents {
    data class ShowSnackBar(val message: String, val isSaving: Boolean) : AddBookScreenUiEvents()

    object Navigate : AddBookScreenUiEvents()

}


sealed class GoogleBottomSheetUiState {
    object IsLoading : GoogleBottomSheetUiState()

    object StandbyState : GoogleBottomSheetUiState()

    data class HasFetched(val booksList: List<Book>) : GoogleBottomSheetUiState()

    data class HasFailed(val errorMessage: String) : GoogleBottomSheetUiState()

}

data class ImageSelectorUiState(
    //image could be uri , subject to change
    val imageSelectedUri: Uri = Uri.EMPTY,
    val imageUrl: String = "",
    val showProgress: Boolean = false,
    val isError: Boolean = false
)