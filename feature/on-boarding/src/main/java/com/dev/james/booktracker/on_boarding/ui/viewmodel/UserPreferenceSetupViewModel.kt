package com.dev.james.booktracker.on_boarding.ui.viewmodel

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.dev.james.booktracker.on_boarding.ui.screens.ThemeConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserPreferenceSetupViewModel @Inject constructor() : ViewModel() {

    private var _prefScreenState: MutableStateFlow<UserPrefScreenState> = MutableStateFlow(
        UserPrefScreenState()
    )
    val prefScreenState get() = _prefScreenState.asStateFlow()

    var selectedGenresList = mutableStateListOf<String>()



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
                    currentSelectedAvatar = userPreferenceSetupActions.avatar ,
                    avatarSelectionError = null
                )
            }

            is UserPreferenceSetupActions.AddSelectedGenre -> {
                val selectedGenre = userPreferenceSetupActions.genre
                if(selectedGenresList.contains(userPreferenceSetupActions.genre)){
                    selectedGenresList.remove(selectedGenre)
                    _prefScreenState.value = _prefScreenState.value.copy(
                        genreList = selectedGenresList.toList() ,
                        chipSelectionError = null
                    )
                }else {
                    if (selectedGenresList.size != 5) {
                        selectedGenresList.add(selectedGenre)
                        _prefScreenState.value = _prefScreenState.value.copy(
                            genreList = selectedGenresList.toList() ,
                            chipSelectionError = null
                        )
                    } else {
                        _prefScreenState.value = _prefScreenState.value.copy(
                            genreList = selectedGenresList.toList() ,
                            chipSelectionError = "You are only allowed a selection of 5 genres."
                        )
                    }

                }

            }

            is UserPreferenceSetupActions.SelectTheme -> {
                _prefScreenState.value = _prefScreenState.value.copy(
                    currentSelectedTheme = userPreferenceSetupActions.theme
                )
            }

            is UserPreferenceSetupActions.SavePreferenceData -> {
                if(_prefScreenState.value.userName.isBlank()){
                    _prefScreenState.value = _prefScreenState.value.copy(
                        userName = "",
                        userNameFieldError = "Username should not be empty" ,
                        currentPosition = 0
                    )
                    return
                }
                if(_prefScreenState.value.currentSelectedAvatar == 0){
                    _prefScreenState.value = _prefScreenState.value.copy(
                        avatarSelectionError = "Hey ${_prefScreenState.value.userName} ! Do not forget your avatar."  ,
                        currentPosition = 1
                    )
                    return
                }

                if(_prefScreenState.value.genreList.isEmpty()){
                    _prefScreenState.value = _prefScreenState.value.copy(
                        chipSelectionError = "Come on ${_prefScreenState.value.userName} ! What about your favourite genres?" ,
                        currentPosition = 2
                    )
                    return
                }
                Timber.d("Saving user data to database")
            }

            is UserPreferenceSetupActions.MoveNext -> {
                val currentPosition = userPreferenceSetupActions.currentPos
                if (currentPosition < 3) {
                    _prefScreenState.value = _prefScreenState.value.copy(
                        previousPosition = currentPosition ,
                        currentPosition = currentPosition + 1
                    )
                }
            }

            is UserPreferenceSetupActions.MovePrevious -> {
                val currentPosition = userPreferenceSetupActions.currentPos
                if (currentPosition > 0) {
                    _prefScreenState.value = _prefScreenState.value.copy(
                        previousPosition = currentPosition ,
                        currentPosition = currentPosition - 1
                    )
                }
            }
        }
    }


    sealed class UserPreferenceSetupActions {
        data class UpdateUserName(val name: String) : UserPreferenceSetupActions()
        data class SelectAvatar(val avatar: Int) : UserPreferenceSetupActions()
        data class AddSelectedGenre(val genre : String) : UserPreferenceSetupActions()
        data class SelectTheme(val theme : Int) : UserPreferenceSetupActions()
        object SavePreferenceData : UserPreferenceSetupActions()
        data class MoveNext(val currentPos : Int) : UserPreferenceSetupActions()
        data class MovePrevious(val currentPos : Int) : UserPreferenceSetupActions()



    }

}


data class UserPrefScreenState(
    val userName: String = "",
    val currentSelectedAvatar: Int = 0,
    val genreList: List<String> = emptyList(),
    val currentSelectedTheme: Int = ThemeConstants.SYSTEM_DEFAULT,
    val userNameFieldError : String? = null ,
    val chipSelectionError : String? = null ,
    val avatarSelectionError : String? = null ,
    val currentPosition : Int = 0 ,
    val previousPosition : Int = 0
)