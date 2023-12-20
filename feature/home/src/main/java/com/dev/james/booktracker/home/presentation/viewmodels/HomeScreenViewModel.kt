package com.dev.james.booktracker.home.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.Goal
import com.dev.james.booktracker.core.common_models.GoalProgressData
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dev.james.domain.usecases.FetchGoalProgress
import com.dev.james.domain.usecases.FetchPdfBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
  private val fetchActiveBookProgress: FetchActiveBookProgress ,
  private val fetchGoalProgress: FetchGoalProgress ,
  private val fetchPdfBooks: FetchPdfBooks
) : ViewModel() {

  private var _homeScreenUiState : MutableStateFlow<HomeScreenUiState>  = MutableStateFlow(
    HomeScreenUiState.HasFetchedScreenData(
      BookProgressData() ,
      GoalProgressData()
    )
  )
  val homeScreenUiState get() = _homeScreenUiState.asStateFlow()


  init {
    viewModelScope.launch {
      val bookGoal = fetchActiveBookProgress.invoke(null)
      val goalProgress = fetchGoalProgress.invoke()
      _homeScreenUiState.value = HomeScreenUiState.HasFetchedScreenData(
        bookProgressData = bookGoal ,
        goalProgressData = goalProgress
      )
    }
  }
  fun getCachedPdfs(){
    //test run to see if pdfs will be fetched
    viewModelScope.launch {
      fetchPdfBooks.invoke()
    }

  }

  sealed class HomeScreenUiState {
    data class HasFetchedScreenData(
      val bookProgressData: BookProgressData ,
      val goalProgressData: GoalProgressData
    ) : HomeScreenUiState()
  }
}