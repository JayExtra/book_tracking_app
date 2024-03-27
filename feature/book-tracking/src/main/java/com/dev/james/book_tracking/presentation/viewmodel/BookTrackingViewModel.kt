package com.dev.james.book_tracking.presentation.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.GoalProgressData
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dev.james.domain.usecases.FetchGoalProgress
import com.dev.james.domain.usecases.LogProgressUsecase
import com.dev.james.domain.usecases.SetActiveBookUsecase
import com.dev.james.domain.usecases.UpdateGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookTrackingViewModel @Inject constructor(
    private val fetchActiveBookProgress: FetchActiveBookProgress,
    private val fetchGoalProgress: FetchGoalProgress,
    private val logProgressUsecase: LogProgressUsecase,
    private val setActiveBookUsecase: SetActiveBookUsecase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val readingListsRepository: ReadingListsRepository ,
    private val booksRepository: BooksRepository
) : ViewModel() {

    companion object {
        const val TAG = "BookTrackingViewModel"
    }

    private var _bookStatsState: MutableStateFlow<BookProgressData> =
        MutableStateFlow(BookProgressData())
    val bookStatsState get() = _bookStatsState.asStateFlow()

    private var _goalProgressState: MutableStateFlow<GoalProgressData> =
        MutableStateFlow(GoalProgressData())
    val goalProgressState get() = _goalProgressState.asStateFlow()

    private var _readingLists : MutableStateFlow<List<ReadingListUiItem>> =
        MutableStateFlow(listOf())
    val readingLists get() = _readingLists.asStateFlow()


    private val _trackBookScreenUiEvents : Channel<TrackBookScreenUiEvents> = Channel()
    val trackBookScreenUiEvents get() = _trackBookScreenUiEvents.receiveAsFlow()
    var showBookCompleteDialog by mutableStateOf(false)
        private set

    @RequiresApi(Build.VERSION_CODES.N)
    fun getBookStatistics(bookId: String) = viewModelScope.launch {
        _bookStatsState.value = fetchActiveBookProgress.invoke(bookId)
        _goalProgressState.value = fetchGoalProgress.invoke()
    }

    fun fetchReadingLists(bookId : String) = viewModelScope.launch {
        readingListsRepository.getReadingLists().collectLatest {
           val readingListUiItem =  it.map { readingList ->
                   ReadingListUiItem(
                       id = readingList.id ,
                       name = readingList.name ,
                       isInList = readingList.readingList.contains(bookId)
                   )
            }
            _readingLists.value = readingListUiItem
        }

    }


    fun setActiveBook(bookId: String) = viewModelScope.launch {
        setActiveBookUsecase.invoke(bookId)
    }

    fun addOrRemoveFromReadingList(bookId: String, readingListId : String, readingListName : String) = viewModelScope.launch {

        val result = booksRepository.addOrRemoveBookFromReadingList(
            readingListId = readingListId ,
            bookId = bookId
        )

        when(result){
            is Resource.Success -> {
                val addOrRemove = result.data?.split(":")!!
                when {
                    addOrRemove[0] == "added" -> {
                        //show snackbar
                        _trackBookScreenUiEvents.send(
                            TrackBookScreenUiEvents.ShowSnackbar(
                                message = "Added to $readingListName"
                            )
                        )
                    }
                    addOrRemove[0] == "removed" -> {
                        _trackBookScreenUiEvents.send(
                            TrackBookScreenUiEvents.ShowSnackbar(
                                message = "Removed from $readingListName"
                            )
                        )
                    }
                    else -> {}
                }
            }
            is Resource.Error -> {
                //show error snackbar
                _trackBookScreenUiEvents.send(
                    TrackBookScreenUiEvents.ShowSnackbar(
                        message = "Could not add or remove the book from the reading list."
                    )
                )
            }
            else -> {}
        }

    }

    fun updateTotalBooksReadCount() = viewModelScope.launch {
        updateGoalUseCase.addBooksReadTotal()
    }

    @SuppressLint("NewApi")
    fun logProgress(
        bookId: String,
        timeTaken: Long,
        chapterTitle: String,
        currentPage: Int,
        chapterNumber: Int
    ) = viewModelScope.launch {

        Timber.tag(TAG).d("logProgress triggered!")
        logProgressUsecase.logCurrentProgress(
            bookId = bookId,
            readingPeriod = timeTaken,
            currentChapterTitle = chapterTitle,
            currentPage = currentPage,
            chapterNumber = chapterNumber,
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

        val bookStats = fetchActiveBookProgress.invoke(bookId)
        Timber.tag(TAG).d("My current progress ${bookStats.progress}")
        if (bookStats.progress >= 1f) {
            /* _trackBookScreenUiEvents.send(
                 TrackBookScreenUiEvents.ShowBookCompleteDialog
             )*/
            showBookCompleteDialog = true
        }
    }

    fun dismissDialog() {
        showBookCompleteDialog = false
    }

     sealed class TrackBookScreenUiEvents {
         data class ShowSnackbar(val message : String) : TrackBookScreenUiEvents()
         object DefaultState : TrackBookScreenUiEvents()
     }
}

data class ReadingListUiItem(
    val id: String = "",
    val name: String = "" ,
    val isInList : Boolean = false
)

