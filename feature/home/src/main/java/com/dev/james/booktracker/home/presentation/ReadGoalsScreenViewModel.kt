package com.dev.james.booktracker.home.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.dsc.form_builder.FormState
import com.dsc.form_builder.SelectState
import com.dsc.form_builder.TextFieldState
import com.dsc.form_builder.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReadGoalsScreenViewModel @Inject constructor() : ViewModel() {

    val currentReadFormState = FormState(
        fields = listOf(
            TextFieldState(
                name = "title" ,
                validators = listOf(Validators.Required(message = "Book title is required."))
            ) ,
            TextFieldState(name = "author" , validators = listOf(Validators.Required(message = "An author for the book is needed."))) ,
            TextFieldState(
                name = "chapters" ,
                validators = listOf(Validators.Required(message = "Please specify the number of chapters."))
            ) ,
            TextFieldState(
                name = "current chapter" ,
                validators = listOf(Validators.Required(message = "Please specify the current chapter you are in."))
            ) ,
            TextFieldState(
                name = "chapter title" ,
                validators = listOf(Validators.Required(message = "Please specify the current chapter title."))
            ) ,
        )
    )

    override fun onCleared() {
        super.onCleared()
    }

    /*Add Read form ui actions*/
    sealed class AddReadFormUiActions {

    }

    /*General screen ui actions*/
    sealed class ReadGoalsUiActions {

    }

    data class ReadGoalsScreenState(
        val currentPosition : Int = 0 ,
        val previousPosition : Int = 0
    )
}