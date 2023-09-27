package com.dev.james.booktracker.on_boarding.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.domain.repository.onboarding.OnBoardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingWelcomeScreenViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel() {

    fun finishOnBoardingStatus() =
        viewModelScope.launch {
            onBoardingRepository.updateOnBoardingStatus(status = true)
        }
}