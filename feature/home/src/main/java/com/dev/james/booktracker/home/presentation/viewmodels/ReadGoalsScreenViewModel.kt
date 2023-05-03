package com.dev.james.booktracker.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.SelectState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReadGoalsScreenViewModel @Inject constructor() : ViewModel() {

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
                validators = listOf(Validators.Required(message = "Please specify the number of chapters."))
            ),
            TextFieldState(
                name = "current chapter",
                validators = listOf(Validators.Required(message = "Please specify the current chapter you are in."))
            ),
            TextFieldState(
                name = "chapter title",
                validators = listOf(Validators.Required(message = "Please specify the current chapter title."))
            ),
        )
    )

    val overallGoalFormState = FormState(

        fields = listOf(
            TextFieldState(
                name = "time",
                validators = listOf(Validators.Required(message = "Please provide minimum times"))
            ) ,
            ChoiceState(
                name = "frequency field" ,
                validators = listOf(Validators.Required())
            ) ,
            SelectState(
                name = "specific days" ,
                validators = listOf(Validators.Required( message = "You should provide specific days for alerts") , Validators.Min(limit = 1 , "Specified days should not be empty. "))
            ) ,
            TextFieldState(
                name = "alert note" ,
            )
        )

    )

    fun validateImageSelected(imageSelectedUri: String) {
        if (imageSelectedUri.isBlank()) {
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

    override fun onCleared() {
        super.onCleared()
    }

    /*Add Read form ui actions*/
    sealed class AddReadFormUiActions {
        object LaunchImagePicker : AddReadFormUiActions()
        data class ImageSelected(val imageUri: String) : AddReadFormUiActions()
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
    val imageSelectedUri: String = "",
    val showProgress: Boolean = false,
    val isError: Boolean = false
)