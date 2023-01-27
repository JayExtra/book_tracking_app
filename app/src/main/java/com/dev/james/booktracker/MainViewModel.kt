package com.dev.james.booktracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.domain.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(value = true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _isOnBoarded = MutableStateFlow(value = false)
    val isOnBoarded get() = _isOnBoarded.asStateFlow()


    init {
        viewModelScope.launch {
            delay(2000L)
            _isLoading.value = false
        }
        getOnBoardingStatus()
    }

   private fun getOnBoardingStatus(){
       viewModelScope.launch {
           Timber.d("on boarding value => ${mainRepository.getOnBoardingStatus()}")
           _isOnBoarded.value = mainRepository.getOnBoardingStatus()
       }
       /*viewModelScope.launch {
           _isOnBoarded.value = mainRepository.getOnBoardingStatus()
       }*/
   }


}