package com.dev.james.my_library.presentation.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReadingListScreenViewModel @Inject constructor(
) : ViewModel() {
    var state by mutableStateOf(ReadingListScreenState())
        private set

    fun onAction(action : ReadingListScreenAction) {
        when(action) {
            is ReadingListScreenAction.DefaultAction -> {}        }
    }


    sealed class ReadingListScreenAction {
       object DefaultAction : ReadingListScreenAction()
    }

    data class ReadingListScreenState(
        val isLoading : Boolean = false
    )

}
