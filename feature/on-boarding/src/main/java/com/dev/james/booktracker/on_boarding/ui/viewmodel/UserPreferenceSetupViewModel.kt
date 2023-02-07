package com.dev.james.booktracker.on_boarding.ui.viewmodel

import android.hardware.biometrics.BiometricManager.Strings
import androidx.lifecycle.ViewModel
import com.dev.james.booktracker.on_boarding.ui.screens.ThemeConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import javax.inject.Inject

@HiltViewModel
class UserPreferenceSetupViewModel @Inject constructor() : ViewModel() {

    private var _prefScreenState: MutableStateFlow<UserPrefScreenState> = MutableStateFlow(
        UserPrefScreenState()
    )
    val prefScreenState get() = _prefScreenState.asStateFlow()

    fun setUserPreference(userPreferenceSetupActions: UserPreferenceSetupActions) {
        when (userPreferenceSetupActions) {
            is UserPreferenceSetupActions.UpdateUserName -> {
                if (userPreferenceSetupActions.name.isBlank()) {
                    _prefScreenState.value = _prefScreenState.value.copy(
                        userName = "",
                        userNameFieldError = "Username should not be empty"
                    )
                } else {
                    _prefScreenState.value = _prefScreenState.value.copy(
                        userName = userPreferenceSetupActions.name,
                        userNameFieldError = null
                    )
                }

            }
            is UserPreferenceSetupActions.SelectAvatar -> {
                _prefScreenState.value = _prefScreenState.value.copy(
                    currentSelectedAvatar = userPreferenceSetupActions.avatar
                )
            }

            is UserPreferenceSetupActions.AddGenresList -> {
                _prefScreenState.value = _prefScreenState.value.copy(
                    genreList = userPreferenceSetupActions.genreList
                )
            }

            is UserPreferenceSetupActions.SelectTheme -> {
                _prefScreenState.value = _prefScreenState.value.copy(
                    currentSelectedTheme = userPreferenceSetupActions.theme
                )
            }
        }
    }


    sealed class UserPreferenceSetupActions {
        data class UpdateUserName(val name: String) : UserPreferenceSetupActions()
        data class SelectAvatar(val avatar: Int) : UserPreferenceSetupActions()
        data class AddGenresList(val genreList: List<String>) : UserPreferenceSetupActions()
        data class SelectTheme(val theme : Int) : UserPreferenceSetupActions()


    }

}


data class UserPrefScreenState(
    val userName: String = "",
    val currentSelectedAvatar: Int = 0,
    val genreList: List<String> = emptyList(),
    val currentSelectedTheme: Int = ThemeConstants.SYSTEM_DEFAULT,
    val userNameFieldError: String? = null
)