package com.dev.james.booktracker.home.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.mappers.mapToBookDomainObject
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.home.data.repository.BooksRemoteRepository
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReadGoalsScreenViewModel @Inject constructor(
    private val booksRemoteRepository: BooksRemoteRepository
) : ViewModel() {

    companion object {
        const val TAG = "ReadGoalsScreenViewModel"
    }

    private var queryJob : Job? = null

    private val _imageSelectorState: MutableStateFlow<ImageSelectorUiState> = MutableStateFlow(
        ImageSelectorUiState()
    )
    val imageSelectorUiState get() = _imageSelectorState.asStateFlow()

    private val _readGoalsScreenUiState: MutableStateFlow<ReadGoalsScreenState> = MutableStateFlow(
        ReadGoalsScreenState()
    )
    val readGoalsScreenUiState get() = _readGoalsScreenUiState

    private val searchQueryMutableStateFlow : MutableStateFlow<String> = MutableStateFlow("Think Big")

    private var _googleBottomSheetSearchState : MutableStateFlow<GoogleBottomSheetUiState> = MutableStateFlow(GoogleBottomSheetUiState.StandbyState)
    val  googleBottomSheetSearchState get() = _googleBottomSheetSearchState


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
            )
        )

    )


    val specificGoalsFormState = FormState(
        fields = listOf(
            TextFieldState(
                name = "books_month",
                transform = { it.toInt() },
                validators = listOf(
                    Validators.Required(message = "Please provide number of books books"),
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
                name = "search_field" ,
                initial = ""
            )
        )
    )

    fun validateImageSelected(imageSelectedUri: Uri) {
        if (imageSelectedUri == Uri.EMPTY) {
            _imageSelectorState.value = _imageSelectorState.value.copy(
                showProgress = false,
                isError = true
            )
        } else {
            _imageSelectorState.value = _imageSelectorState.value.copy(
                showProgress = false,
                isError = false,
                imageSelectedUri = imageSelectedUri
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
                    imageSelectedUri = Uri.EMPTY
                )
            }
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
                    Timber.tag(TAG).d("Saving user goals in db")
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
        }
    }

    //google search functionality action
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun searchForBook(searchQuery : String) {
        queryJob = viewModelScope.launch {
            searchQueryMutableStateFlow.value = searchQuery
            searchQueryMutableStateFlow
                .debounce(200)
                .filter { query ->
                    if (query.isEmpty()) {
                        val searchViewState = bottomSheetSearchFieldState.getState<TextFieldState>("search_field")
                        searchViewState.change("")
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    booksRemoteRepository.getBooksFromApi(bookTitle = query , bookAuthor = "")
                }
                .flowOn(Dispatchers.Default)
                .collect { resource ->
                    when(resource){
                        is Resource.Success -> {
                            val booksList = resource.data?.items
                            if(!booksList.isNullOrEmpty()){
                                _googleBottomSheetSearchState.value = GoogleBottomSheetUiState.HasFetched(
                                    booksList =   booksList.map { bookDto -> bookDto.mapToBookDomainObject() }
                                )
                            }else {
                                _googleBottomSheetSearchState.value = GoogleBottomSheetUiState
                                    .HasFetched(
                                        booksList = emptyList()
                                    )
                            }
                            Timber.tag(TAG).d(booksList.toString())

                        }
                        is Resource.Error -> {
                            val errorMessage = resource.message ?: "Oops! Something went wrong"
                            _googleBottomSheetSearchState.value = GoogleBottomSheetUiState.HasFailed(
                                errorMessage = errorMessage
                            )
                        }
                        is Resource.Loading -> {
                            _googleBottomSheetSearchState.value = GoogleBottomSheetUiState.IsLoading
                        }
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
    }

    /*Add Read form ui actions*/
    sealed class AddReadFormUiActions {
        object LaunchImagePicker : AddReadFormUiActions()
        data class ImageSelected(val imageUri: Uri) : AddReadFormUiActions()
        object DismissImagePicker : AddReadFormUiActions()

        object ClearImage : AddReadFormUiActions()
    }

    /*General screen ui actions*/
    sealed class ReadGoalsUiActions {
        data class MoveNext(val currentPosition: Int) : ReadGoalsUiActions()
        data class MovePrevious(val currentPosition: Int) : ReadGoalsUiActions()
    }

    sealed class GoogleBottomSheetUiState {
        object IsLoading : GoogleBottomSheetUiState()

        object StandbyState : GoogleBottomSheetUiState()

        data class HasFetched(val booksList : List<Book>) : GoogleBottomSheetUiState()

        data class HasFailed(val errorMessage : String ) : GoogleBottomSheetUiState()

    }

}

data class ReadGoalsScreenState(
    val currentPosition: Int = 0,
    val previousPosition: Int = 0
)

data class ImageSelectorUiState(
    //image could be uri , subject to change
    val imageSelectedUri: Uri = Uri.EMPTY,
    val showProgress: Boolean = false,
    val isError: Boolean = false
)