package com.dev.james.booktracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(value = true)
    val isLoading get() = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000L)
            _isLoading.value = false
        }
    }
}