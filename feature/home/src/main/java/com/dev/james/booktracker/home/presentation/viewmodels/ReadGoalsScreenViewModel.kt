package com.dev.james.booktracker.home.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.utilities.ConnectivityManager
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.home.data.repository.BooksRemoteRepository
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.SelectState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Transform
import com.dsc.form_builder.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReadGoalsScreenViewModel @Inject constructor(
    private val booksRemoteRepository: BooksRemoteRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    companion object {
        const val TAG = "ReadGoalsScreenViewModel"
    }

    private val _imageSelectorState: MutableStateFlow<ImageSelectorUiState> = MutableStateFlow(
        ImageSelectorUiState()
    )
    val imageSelectorUiState get() = _imageSelectorState.asStateFlow()

    private val _readGoalsScreenUiState: MutableStateFlow<ReadGoalsScreenState> = MutableStateFlow(
        ReadGoalsScreenState()
    )
    val readGoalsScreenUiState get() = _readGoalsScreenUiState


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

            is AddReadFormUiActions.ImageSelected -> {

                _imageSelectorState.value = _imageSelectorState.value.copy(
                    showProgress = false,
                    isError = false,
                    imageSelectedUri = action.imageUri
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

    //google search action
    fun searchForBook() {
        viewModelScope.launch {
            if(connectivityManager.getNetworkStatus()){
            when (
                val result = booksRemoteRepository.getBooksFromApi(
                    bookTitle = "Think Big",
                    bookAuthor = ""
                )
            ) {
                is Resource.Success -> {
                    Timber.tag(TAG).d("Books found => ${result.data}")
                }

                is Resource.Error -> {
                    Timber.tag(TAG).d("Books found => ${result.message}")
                }

                else -> {}

            }
            }else {
                Timber.tag(TAG).d("No network available currently")
                //show no network error dialog or message

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    /*Add Read form ui actions*/
    sealed class AddReadFormUiActions {
        object LaunchImagePicker : AddReadFormUiActions()
        data class ImageSelected(val imageUri: Uri) : AddReadFormUiActions()
    }

    /*General screen ui actions*/
    sealed class ReadGoalsUiActions {
        data class MoveNext(val currentPosition: Int) : ReadGoalsUiActions()
        data class MovePrevious(val currentPosition: Int) : ReadGoalsUiActions()
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