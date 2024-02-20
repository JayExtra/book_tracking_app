package com.dev.james.my_library.presentation.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.domain.usecases.GetBooksAndProgressUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyLibraryViewModel @Inject constructor(
    private val getBooksAndProgressUsecase: GetBooksAndProgressUsecase
) : ViewModel() {

    private var _booksWithProgress : MutableStateFlow<List<LibraryBookData>> = MutableStateFlow(value = listOf(LibraryBookData()))
    val booksWithProgress get() = _booksWithProgress.asStateFlow()

    var isLoading by mutableStateOf(value = false)
        private set
    init {
        isLoading = true
        getBooksWithProgress()
    }

    private fun getBooksWithProgress() = viewModelScope.launch {
        getBooksAndProgressUsecase.invoke().collectLatest { booksList ->
            isLoading = false
            _booksWithProgress.value = booksList
        }
    }
}