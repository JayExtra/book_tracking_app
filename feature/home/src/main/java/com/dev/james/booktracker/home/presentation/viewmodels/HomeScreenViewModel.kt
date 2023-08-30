package com.dev.james.booktracker.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.BookGoalData
import com.dev.james.booktracker.home.domain.usecase.FetchBookGoalLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
  private val fetchBookGoalLogsUseCase: FetchBookGoalLogsUseCase
) : ViewModel() {

  private var _homeScreenUiState : MutableStateFlow<HomeScreenUiState>  = MutableStateFlow(
    HomeScreenUiState.HasFetchedScreenData(
      BookGoalData()
    )
  )
  val homeScreenUiState get() = _homeScreenUiState.asStateFlow()


  init {
    viewModelScope.launch {
      val bookGoal = fetchBookGoalLogsUseCase.invoke()
      _homeScreenUiState.value = HomeScreenUiState.HasFetchedScreenData(
        bookGoalData = bookGoal
      )
    }
  }

  sealed class HomeScreenUiState {
    data class HasFetchedScreenData(
      val bookGoalData: BookGoalData
    ) : HomeScreenUiState()
  }
}