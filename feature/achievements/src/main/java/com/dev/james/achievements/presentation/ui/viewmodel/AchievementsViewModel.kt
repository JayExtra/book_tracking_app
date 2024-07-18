package com.dev.james.achievements.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AchievementsViewModel @Inject constructor(
) : ViewModel() {

    private var _state : MutableStateFlow<AchievementsScreenUIState> = MutableStateFlow(
        AchievementsScreenUIState()
    )
    val state get() = _state.asStateFlow()

    fun onAction(action : AchievementsScreenUIActions) {
        when(action) {
            is AchievementsScreenUIActions.DefaultScreenUIActions -> {}
        }
    }
    sealed class AchievementsScreenUIActions {
        object DefaultScreenUIActions : AchievementsScreenUIActions()
    }

}

 data class AchievementsScreenUIState(
    val isLoading : Boolean = false
 )