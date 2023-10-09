package com.dev.james.booktracker.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.domain.usecases.FetchActiveBookProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
  private val fetchActiveBookProgress: FetchActiveBookProgress
) : ViewModel() {

  private var _homeScreenUiState : MutableStateFlow<HomeScreenUiState>  = MutableStateFlow(
    HomeScreenUiState.HasFetchedScreenData(
      BookProgressData()
    )
  )
  val homeScreenUiState get() = _homeScreenUiState.asStateFlow()


  init {
    viewModelScope.launch {
      val bookGoal = fetchActiveBookProgress.invoke()
      _homeScreenUiState.value = HomeScreenUiState.HasFetchedScreenData(
        bookProgressData = bookGoal
      )
    }
  }

  sealed class HomeScreenUiState {
    data class HasFetchedScreenData(
      val bookProgressData: BookProgressData
    ) : HomeScreenUiState()
  }
}