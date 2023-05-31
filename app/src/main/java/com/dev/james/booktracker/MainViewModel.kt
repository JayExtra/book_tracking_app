package com.dev.james.booktracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.core.user_preferences.data.models.UserDetails
import com.dev.james.booktracker.core.user_preferences.domain.repo.UserPreferencesRepository
import com.dev.james.booktracker.domain.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository ,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    val isLoading get() = _isLoading.asStateFlow()

   /* private val _isOnBoarded = MutableStateFlow(value = false)*/

    val isOnBoarded = userPreferencesRepository.getOnBoardingStatus()
        .stateIn(
            scope = viewModelScope ,
            started = SharingStarted.WhileSubscribed(5000L) ,
            initialValue = false
        )

    val theme = userPreferencesRepository.getSelectedTheme()
        .stateIn(
            scope = viewModelScope ,
            started = SharingStarted.WhileSubscribed(5000L) ,
            initialValue = Theme.FOLLOW_SYSTEM.themeValue
        )

    val user = userPreferencesRepository.getUserDetails()
        .stateIn(
            scope = viewModelScope ,
            started = SharingStarted.WhileSubscribed(5000L) ,
            initialValue =  UserDetails("fetching.." , emptyList() , selectedAvatar = R.drawable.round_account_circle_24)
        )

    init {
        viewModelScope.launch {
            delay(2000L)
            _isLoading.value = false
        }
    }

  /* private fun getOnBoardingStatus(){
       viewModelScope.launch {
           Timber.d("on boarding value => ${mainRepository.getOnBoardingStatus()}")
           _isOnBoarded.value = mainRepository.getOnBoardingStatus()
       }
       *//*viewModelScope.launch {
           _isOnBoarded.value = mainRepository.getOnBoardingStatus()
       }*//*
   }*/


}