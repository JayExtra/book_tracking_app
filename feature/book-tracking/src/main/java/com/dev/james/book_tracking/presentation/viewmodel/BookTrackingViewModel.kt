package com.dev.james.book_tracking.presentation.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.common_models.GoalProgressData
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dev.james.domain.usecases.FetchGoalProgress
import com.dev.james.domain.usecases.LogProgressUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookTrackingViewModel @Inject constructor(
    private val fetchActiveBookProgress : FetchActiveBookProgress ,
    private val fetchGoalProgress: FetchGoalProgress ,
    private val logProgressUsecase: LogProgressUsecase
) : ViewModel() {

    companion object {
        const val TAG  = "BookTrackingViewModel"
    }

    private var _bookStatsState : MutableStateFlow<BookProgressData> = MutableStateFlow(BookProgressData())
    val bookStatsState get() = _bookStatsState.asStateFlow()

    private var _goalProgressState : MutableStateFlow<GoalProgressData> = MutableStateFlow(GoalProgressData())
    val goalProgressState get() = _goalProgressState.asStateFlow()
/*
    private val _trackBookScreenUiEvents : Channel<TrackBookScreenUiEvents> = Channel()
    val trackBookScreenUiEvents get() = _trackBookScreenUiEvents.receiveAsFlow()*/

    @RequiresApi(Build.VERSION_CODES.N)
    fun getBookStatistics(bookId : String) = viewModelScope.launch {
       _bookStatsState.value =  fetchActiveBookProgress.invoke(bookId)
        _goalProgressState.value = fetchGoalProgress.invoke()
    }


    @SuppressLint("NewApi")
    fun logProgress(bookId: String, timeTaken : Long, chapterTitle : String, currentPage : Int, chapterNumber: Int) = viewModelScope.launch {

        Timber.tag(TAG).d("logProgress triggered!")
        logProgressUsecase.logCurrentProgress(
            bookId = bookId ,
            readingPeriod = timeTaken ,
            currentChapterTitle = chapterTitle ,
            currentPage = currentPage ,
            chapterNumber = chapterNumber ,
            onSaveComplete = {
                Timber.tag(TAG).d("success logging => $it")
               /* viewModelScope.launch {
                    _trackBookScreenUiEvents.send(
                        TrackBookScreenUiEvents.CloseLogDialog
                    )
                }*/
            }
        )
        getBookStatistics(bookId = bookId)
    }

}

sealed class TrackBookScreenUiEvents {
    object CloseLogDialog : TrackBookScreenUiEvents()
    object DefaultState : TrackBookScreenUiEvents()
}