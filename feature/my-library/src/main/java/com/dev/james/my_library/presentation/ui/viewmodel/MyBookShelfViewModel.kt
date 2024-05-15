package com.dev.james.my_library.presentation.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyBookShelfViewModel @Inject constructor() : ViewModel() {
    private val _screenState : MutableStateFlow<BookShelfUIState> = MutableStateFlow(
        BookShelfUIState()
    )
    val screenState get() = _screenState.asStateFlow()

    fun onAction(action : BookShelfUIActions){
        when(action){
            is BookShelfUIActions.FetchData -> {

            }
        }
    }
    sealed class BookShelfUIActions {
        object FetchData : BookShelfUIActions()
    }


}
data class BookShelfUIState(
    val isLoading : Boolean = false
)

