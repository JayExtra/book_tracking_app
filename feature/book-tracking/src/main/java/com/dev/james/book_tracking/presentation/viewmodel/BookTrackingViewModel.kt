package com.dev.james.book_tracking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.BookStatsData
import com.dev.james.domain.usecases.book_tracking.FetchBookWithLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookTrackingViewModel @Inject constructor(
    private val fetchBookWithLogsUseCase : FetchBookWithLogsUseCase
) : ViewModel() {

    private var _bookStatsState : MutableStateFlow<BookStatsData> = MutableStateFlow(BookStatsData())
    val bookStatsState get() = _bookStatsState.asStateFlow()

    fun getBookStatistics(bookId : String) = viewModelScope.launch {
       _bookStatsState.value =  fetchBookWithLogsUseCase.invoke(bookId)
    }
}