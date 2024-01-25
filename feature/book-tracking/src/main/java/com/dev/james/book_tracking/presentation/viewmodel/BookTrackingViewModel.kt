package com.dev.james.book_tracking.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.BookStatsData
import com.dev.james.domain.usecases.FetchActiveBookProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookTrackingViewModel @Inject constructor(
    private val fetchActiveBookProgress : FetchActiveBookProgress
) : ViewModel() {

    private var _bookStatsState : MutableStateFlow<BookProgressData> = MutableStateFlow(BookProgressData())
    val bookStatsState get() = _bookStatsState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.N)
    fun getBookStatistics(bookId : String) = viewModelScope.launch {
       _bookStatsState.value =  fetchActiveBookProgress.invoke(bookId)
    }

}