package com.dev.james.booktracker.home.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookGoal
import com.dev.james.booktracker.core.common_models.OverallGoal
import com.dev.james.booktracker.core.common_models.SpecificGoal
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.mappers.mapToPresentation
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.utilities.calculateTimeToLong
import com.dev.james.booktracker.core.utilities.convertToAuthorsString
import com.dev.james.booktracker.core.utilities.generateRandomId
import com.dev.james.booktracker.core.utilities.generateSecureUUID
import com.dev.james.booktracker.core.utilities.prepareGoalString
import com.dev.james.booktracker.core_network.dtos.BookDto
import com.dev.james.booktracker.home.domain.repositories.BooksRepository
import com.dev.james.booktracker.home.domain.repositories.GoalsRepository
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.SelectState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReadGoalsScreenViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    companion object {
        const val TAG = "ReadGoalsScreenViewModel"
    }

    private var queryJob: Job? = null

    private val _imageSelectorState: MutableStateFlow<ImageSelectorUiState> = MutableStateFlow(
        ImageSelectorUiState()
    )
    val imageSelectorUiState get() = _imageSelectorState.asStateFlow()

    private val selectedBookState: MutableStateFlow<Book> = MutableStateFlow(
        Book()
    )

    private val savedBookState: MutableStateFlow<BookSave> = MutableStateFlow(
        BookSave()
    )

    private val _readGoalsScreenUiState: MutableStateFlow<ReadGoalsScreenState> = MutableStateFlow(
        ReadGoalsScreenState()
    )
    val readGoalsScreenUiState get() = _readGoalsScreenUiState

    private val searchQueryMutableStateFlow: MutableStateFlow<String> =
        MutableStateFlow("Think Big")

    private var _googleBottomSheetSearchState: MutableStateFlow<GoogleBottomSheetUiState> =
        MutableStateFlow(GoogleBottomSheetUiState.StandbyState)
    val googleBottomSheetSearchState get() = _googleBottomSheetSearchState

    private var _readGoalsScreenUiEvents: Channel<ReadGoalsUiEvents> = Channel()
    val readGoalsScreenUiEvents get() = _readGoalsScreenUiEvents.receiveAsFlow()


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

    val overallGoalFormState = FormState(

        fields = listOf(
            TextFieldState(
                name = "time",
                validators = listOf(
                    Validators.Required(message = "Please provide minimum times")
                )
            ),
            ChoiceState(
                name = "frequency field",
                validators = listOf(Validators.Required())
            ),
            ChoiceState(
                name = "alert_switch",
                initial = "No",
                validators = listOf()
            ),
            SelectState(
                name = "specific days",
                initial = mutableListOf(),
                validators = listOf(
                    Validators.Required(message = "Please select the days you want"),
                    Validators.Min(limit = 1, "Please select your days.")
                )
            ),
            TextFieldState(
                name = "alert note",
            ),
            ChoiceState(
                name = "alert_dialog_time",
                validators = listOf()
            )
        )

    )


    val specificGoalsFormState = FormState(
        fields = listOf(
            TextFieldState(
                name = "books_month",
                transform = { it.toInt() },
                validators = listOf(
                    Validators.Required(message = "Please provide number of books"),
                    Validators.MinValue(limit = 1, message = "")
                ),
            ),
            ChoiceState(
                name = "available_books",
                validators = listOf(Validators.Required(message = "You need to set a goal for an added book."))
            ),
            ChoiceState(
                name = "chapter_hours",
                initial = "By chapters",
                validators = emptyList()
            ),
            TextFieldState(
                name = "time_chapter",
                validators = listOf(
                    Validators.Required(message = "Time or chapter constraint needed for this goal.")
                )
            ),
            ChoiceState(
                name = "period",
                validators = listOf(
                    Validators.Required(message = "Please select how long you want this goal to run.")
                )
            ),
            SelectState(
                name = "period_days",
                initial = mutableListOf(),
                validators = listOf(
                    Validators.Required(message = "Please select the days you want"),
                    Validators.Min(limit = 1, "Please select your days.")
                )
            )
        )
    )

    val bottomSheetSearchFieldState = FormState(
        fields = listOf(
            TextFieldState(
                name = "search_field",
                initial = ""
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

    private val overallGoalTimeFieldState: TextFieldState = overallGoalFormState.getState("time")
    private val overallGoalFrequencyFieldState: ChoiceState =
        overallGoalFormState.getState("frequency field")
    private val overallGoalSpecificFieldState: SelectState =
        overallGoalFormState.getState("specific days")
    private val overallGoalAlertNoteFieldState: TextFieldState =
        overallGoalFormState.getState("alert note")
    private val overallGoalAlertSwitchFieldState: ChoiceState =
        overallGoalFormState.getState("alert_switch")
    private val overallGoalSelectedDialogTime: ChoiceState =
        overallGoalFormState.getState("alert_dialog_time")

    private val specificGoalsBookCountState: TextFieldState =
        specificGoalsFormState.getState("books_month")
    private val availableBooksCountState: ChoiceState =
        specificGoalsFormState.getState("available_books")
    private val chapterOrHoursState: ChoiceState = specificGoalsFormState.getState("chapter_hours")
    private val timeOrChapterState: TextFieldState = specificGoalsFormState.getState("time_chapter")
    private val specificGoalPeriodState: ChoiceState = specificGoalsFormState.getState("period")
    private val periodDaysState: SelectState = specificGoalsFormState.getState("period_days")


    fun validateImageSelected() {
        if (imageSelectorUiState.value.imageSelectedUri != Uri.EMPTY || imageSelectorUiState.value.imageUrl.isNotBlank()) {
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

    fun passAddReadFormAction(action: AddReadFormUiActions) {
        when (action) {
            is AddReadFormUiActions.LaunchImagePicker -> {
                _imageSelectorState.value = _imageSelectorState.value.copy(
                    showProgress = true
                )
            }

            is AddReadFormUiActions.DismissImagePicker -> {
                _imageSelectorState.value = _imageSelectorState.value.copy(
                    showProgress = false
                )
            }

            is AddReadFormUiActions.ImageSelected -> {

                _imageSelectorState.value = _imageSelectorState.value.copy(
                    showProgress = false,
                    isError = false,
                    imageSelectedUri = action.imageUri
                )

            }

            is AddReadFormUiActions.ClearImage -> {
                _imageSelectorState.value = _imageSelectorState.value.copy(
                    imageSelectedUri = Uri.EMPTY,
                    imageUrl = ""
                )

            }

            is AddReadFormUiActions.SaveBook -> {
                saveBookToDb()
            }

            else -> {}
        }
    }

    fun passMainScreenActions(action: ReadGoalsUiActions) {
        when (action) {
            is ReadGoalsUiActions.MoveNext -> {
                val currentPosition = action.currentPosition
                if (currentPosition < 2) {
                    _readGoalsScreenUiState.value = _readGoalsScreenUiState
                        .value.copy(
                            currentPosition = currentPosition + 1,
                            previousPosition = currentPosition
                        )
                } else {
                    //save final goals set here
                    saveUserGoals()
                }
            }

            is ReadGoalsUiActions.MovePrevious -> {
                val currentPosition = action.currentPosition
                if (currentPosition > 0) {
                    _readGoalsScreenUiState.value = _readGoalsScreenUiState
                        .value.copy(
                            currentPosition = currentPosition - 1,
                            previousPosition = currentPosition
                        )
                }
            }

            is ReadGoalsUiActions.UndoBookSave -> {
                Timber.tag(TAG).d("Undo action triggered")
                undoBookSaved()
            }

            else -> {}
        }
    }

    private fun saveBookToDb() = viewModelScope.launch {
        //1.create the book save object , will be determined by whether the image is uri or url
        val author = currentReadFormAuthorFieldState.value
        val title = currentReadFormTitleFieldState.value
        val pages = currentReadFormPagesFieldState.value
        val chapters = currentReadFormChaptersState.value
        val currentChapter = currentReadFormCurrentChapter.value
        val chapterTitle = currentReadFormCurChaptTitleState.value

        val bookId = if (imageSelectorUiState.value.imageSelectedUri != Uri.EMPTY)
            generateSecureUUID()
        else
            selectedBookState.value.bookId ?: "n/a"


        val isUri = imageSelectorUiState.value.imageSelectedUri != Uri.EMPTY

        val bookImage =
            if (isUri) imageSelectorUiState.value.imageSelectedUri.toString() else imageSelectorUiState.value.imageUrl

        val smallThumbnail = selectedBookState.value.bookSmallThumbnail
        val publisher = selectedBookState.value.publisher
        val publishedDate = selectedBookState.value.publishedDate

        val bookSave = BookSave(
            bookId = bookId,
            bookImage = bookImage,
            bookTitle = title,
            bookAuthors = author,
            bookSmallThumbnail = smallThumbnail ?: "n/a",
            bookPagesCount = pages.toInt(),
            publisher = publisher ?: "n/a",
            publishedDate = publishedDate ?: "n/a",
            isUri = isUri,
            chapters = chapters.toInt(),
            currentChapter = currentChapter.toInt(),
            currentChapterTitle = chapterTitle
        )

        //2. save to db
        Timber.tag(TAG).d("Save action triggered")
        if (booksRepository.saveBookToDatabase(bookSave)) {
            savedBookState.value = bookSave
            //show snackbar
            _readGoalsScreenUiEvents.send(
                ReadGoalsUiEvents.ShowSnackBar(
                    message = "${bookSave.bookTitle} added to library.",
                    isSaving = true
                )
            )
            _readGoalsScreenUiState.value = _readGoalsScreenUiState.value.copy(
                shouldDisableNextButton = false
            )
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

            _readGoalsScreenUiEvents.send(
                ReadGoalsUiEvents.ShowSnackBar(
                    message = "${savedBookState.value.bookTitle} removed from library.",
                    isSaving = false
                )
            )
            //reset the book save state
            savedBookState.value = BookSave()

            //hide the next button
            _readGoalsScreenUiState.value = _readGoalsScreenUiState.value.copy(
                shouldDisableNextButton = true
            )
        }
    }

    private fun saveUserGoals() = viewModelScope.launch {
        Timber.tag(TAG).d("Saving user goals in db")
        //save user goals
        val overallGoal = OverallGoal(
            goalId = generateRandomId(10),
            goalInfo = prepareGoalString(
                goalTime = overallGoalTimeFieldState.value,
                condition = overallGoalFrequencyFieldState.value,
                daysList = overallGoalSpecificFieldState.value
            ),
            goalTime = overallGoalTimeFieldState.value.calculateTimeToLong(),
            goalPeriod = overallGoalFrequencyFieldState.value,
            specificDays = if (overallGoalSpecificFieldState.value.isEmpty()) emptyList() else overallGoalSpecificFieldState.value,
            shouldShowAlert = overallGoalAlertSwitchFieldState.value == "Yes",
            alertNote = overallGoalAlertNoteFieldState.value,
            alertTime = overallGoalSelectedDialogTime.value
        )

        val specificGoal = SpecificGoal(
            goalId = generateRandomId(10),
            bookCount = specificGoalsBookCountState.value.toInt(),
            booksReadCount = 0
        )

        val bookGoal = BookGoal(
            bookId = savedBookState.value.bookId,
            isChapterGoal = chapterOrHoursState.value == "By chapters",
            goalInfo = prepareGoalString(
                goalTime = timeOrChapterState.value,
                condition = specificGoalPeriodState.value,
                daysList = periodDaysState.value
            ),
            isTimeGoal = chapterOrHoursState.value == "By hours",
            goalSet = timeOrChapterState.value,
            goalPeriod = specificGoalPeriodState.value,
            specificDays = periodDaysState.value
        )

        when (val result = goalsRepository.saveGoals(overallGoal, specificGoal, bookGoal)) {
            is Resource.Success -> {
                if (result.data == true) {
                    Timber.tag(TAG).d("goals successfully added to database")
                }
            }

            is Resource.Error -> {
                Timber.tag(TAG).d("could not save goals to db. REASON: ${result.message}")
            }

            else -> {}
        }

    }

    fun getCachedBooks() {
        viewModelScope.launch {
            booksRepository.getSavedBooks()
                .map { booksSaveList ->
                    booksSaveList.map {
                        it.mapToPresentation()
                    }
                }.collect { booksList ->
                    _readGoalsScreenUiState.value = _readGoalsScreenUiState.value.copy(
                        savedBooksList = booksList
                    )
                }
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
        selectedBookState.value = book

    }

    //google search functionality action
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

    override fun onCleared() {
        super.onCleared()
        queryJob?.cancel()
        saveBookToDb().cancel()
        undoBookSaved().cancel()
        saveUserGoals().cancel()
    }

    /*Add Read form ui actions*/
    sealed class AddReadFormUiActions {
        object LaunchImagePicker : AddReadFormUiActions()
        data class ImageSelected(val imageUri: Uri) : AddReadFormUiActions()
        object DismissImagePicker : AddReadFormUiActions()

        object ClearImage : AddReadFormUiActions()

        object SaveBook : AddReadFormUiActions()

    }

    /*sealed class GoogleBottomSheetUiActions {
        data class OnBookSelected(
            val book: Book
        ) : GoogleBottomSheetUiState()

    }*/

    /*General screen ui actions*/
    sealed class ReadGoalsUiActions {
        data class MoveNext(val currentPosition: Int) : ReadGoalsUiActions()
        data class MovePrevious(val currentPosition: Int) : ReadGoalsUiActions()
        object UndoBookSave : ReadGoalsUiActions()
    }

    sealed class ReadGoalsUiEvents {
        data class ShowSnackBar(val message: String, val isSaving: Boolean) : ReadGoalsUiEvents()

    }

    sealed class GoogleBottomSheetUiState {
        object IsLoading : GoogleBottomSheetUiState()

        object StandbyState : GoogleBottomSheetUiState()

        data class HasFetched(val booksList: List<Book>) : GoogleBottomSheetUiState()

        data class HasFailed(val errorMessage: String) : GoogleBottomSheetUiState()

    }

}

data class ReadGoalsScreenState(
    val currentPosition: Int = 0,
    val previousPosition: Int = 0,
    val shouldDisableNextButton: Boolean = true,
    val savedBooksList: List<Book> = listOf<Book>()
)

data class ImageSelectorUiState(
    //image could be uri , subject to change
    val imageSelectedUri: Uri = Uri.EMPTY,
    val imageUrl: String = "",
    val showProgress: Boolean = false,
    val isError: Boolean = false
)

fun BookDto.mapToBookUiObject(): Book {
    return Book(
        bookId = id,
        bookImage = volumeInfo?.image_links?.thumbnail,
        bookAuthors = volumeInfo?.authors,
        bookTitle = volumeInfo?.title,
        bookSmallThumbnail = volumeInfo?.image_links?.small_thumbnail,
        bookPagesCount = volumeInfo?.pageCount,
        publishedDate = volumeInfo?.published_date,
        publisher = volumeInfo?.publisher
    )
}
