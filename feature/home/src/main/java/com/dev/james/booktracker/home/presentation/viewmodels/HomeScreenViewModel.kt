package com.dev.james.booktracker.home.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.common_models.GoalProgressData
import com.dev.james.booktracker.core.common_models.PdfBookItem
import com.dev.james.booktracker.core.common_models.mappers.mapToPresentation
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dev.james.domain.usecases.FetchGoalProgress
import com.dev.james.domain.usecases.FetchOrSetStreak
import com.dev.james.domain.usecases.FetchPdfBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
  private val fetchActiveBookProgress: FetchActiveBookProgress ,
  private val fetchGoalProgress: FetchGoalProgress ,
  private val fetchPdfBooks: FetchPdfBooks ,
  private val fetchOrSetStreak: FetchOrSetStreak
) : ViewModel() {

  private var _homeScreenUiState : MutableStateFlow<HomeScreenUiState>  = MutableStateFlow(
    HomeScreenUiState.DefaultState
  )
  val homeScreenUiState get() = _homeScreenUiState.asStateFlow()

  private var _pdfBookListFlow : MutableStateFlow<List<Book>> = MutableStateFlow(listOf(Book()))
  val pdfBookListFlow get() = _pdfBookListFlow.asStateFlow()

  private var fetchPdfsJob : Job? = null

  private var _homeScreenUiEvents : Channel<HomeScreenUiEvent> = Channel()
  val homeScreenUiEvent get() = _homeScreenUiEvents.receiveAsFlow()
    //.stateIn(scope = viewModelScope , started = SharingStarted.WhileSubscribed() , initialValue = HomeScreenUiEvent.NoEvent)


  fun fetchData() = viewModelScope.launch {
    val bookGoal = fetchActiveBookProgress.invoke(null)
    val goalProgress = fetchGoalProgress.invoke()
    val readingStreak = fetchOrSetStreak.fetch(
      onStreakReset = {
        //on event that streak has been reset show a dialog
        viewModelScope.launch {
          _homeScreenUiEvents.send(
            HomeScreenViewModel.HomeScreenUiEvent.ShowStreakResetDialog(
              message = "Your reading streak has been reset!"
            )
          )
        }
      }
    )
    _homeScreenUiState.value = HomeScreenUiState.HasFetchedScreenData(
      bookProgressData = bookGoal ,
      goalProgressData = goalProgress ,
      streakCount = readingStreak
    )
  }
  fun getCachedPdfs(){
    //test run to see if pdfs will be fetched
    fetchPdfsJob?.cancel()
    fetchPdfsJob = viewModelScope.launch {
       fetchPdfBooks.invoke()
         .flowOn(Dispatchers.Main)
        .collectLatest { pdfs ->
            val booksList = pdfs.map {
              it.mapToPresentation()
            }
          _pdfBookListFlow.value = booksList
         }
    }
  }

  override fun onCleared() {
    super.onCleared()
    fetchPdfsJob?.cancel()
  }

  sealed class HomeScreenUiState {
    data class HasFetchedScreenData(
      val bookProgressData: BookProgressData ,
      val goalProgressData: GoalProgressData ,
      val streakCount : Int
    ) : HomeScreenUiState()

    object DefaultState : HomeScreenUiState()
  }

  sealed class HomeScreenUiEvent {
    data class ShowStreakResetDialog(val message:String) : HomeScreenUiEvent()

    object NoEvent : HomeScreenUiEvent()
  }

/*  data class PdfBottomSheetUiState(
    val showCircularProgress : Boolean = false
  )*/

}