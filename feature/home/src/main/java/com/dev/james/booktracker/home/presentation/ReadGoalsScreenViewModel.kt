package com.dev.james.booktracker.home.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
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

    fun passScreenAction(action: AddReadFormUiActions) {
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