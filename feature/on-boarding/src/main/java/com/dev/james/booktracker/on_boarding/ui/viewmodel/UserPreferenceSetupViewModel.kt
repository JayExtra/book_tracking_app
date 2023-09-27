package com.dev.james.booktracker.on_boarding.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.domain.repository.onboarding.OnBoardingRepository
import com.dev.james.booktracker.core.common_models.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserPreferenceSetupViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel() {

    private var _prefScreenState: MutableStateFlow<UserPrefScreenState> = MutableStateFlow(
        UserPrefScreenState()
    )
    val prefScreenState get() = _prefScreenState.asStateFlow()

    private var selectedGenresList = mutableStateListOf<String>()

    private var _uiEvents : Channel<UserPreferenceSetupUiEvents> = Channel()
    val prefScreenUiEvents get() = _uiEvents.receiveAsFlow()



    fun setUserPreference(userPreferenceSetupUiActions: UserPreferenceSetupUiActions) {
        when (userPreferenceSetupUiActions) {
            is UserPreferenceSetupUiActions.UpdateUserNameUi -> {
                if (userPreferenceSetupUiActions.name.isBlank()) {
                    _prefScreenState.value = _prefScreenState.value.copy(
                        userName = "",
                        userNameFieldError = "Username should not be empty"
                    )
                } else {
                    _prefScreenState.value = _prefScreenState.value.copy(
                        userName = userPreferenceSetupUiActions.name,
                        userNameFieldError = null
                    )
                }

            }
            is UserPreferenceSetupUiActions.SelectAvatar -> {
                _prefScreenState.value = _prefScreenState.value.copy(
                    currentSelectedAvatar = userPreferenceSetupUiActions.avatar ,
                    avatarSelectionError = null
                )
            }

            is UserPreferenceSetupUiActions.AddSelectedGenre -> {
                val selectedGenre = userPreferenceSetupUiActions.genre
                if(selectedGenresList.contains(userPreferenceSetupUiActions.genre)){
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

            is UserPreferenceSetupUiActions.SelectTheme -> {
                _prefScreenState.value = _prefScreenState.value.copy(
                    currentSelectedTheme = userPreferenceSetupUiActions.theme
                )
                viewModelScope.launch {
                    onBoardingRepository.saveCurrentTheme(
                        currentTheme =  userPreferenceSetupUiActions.theme
                    )
                }

            }

            is UserPreferenceSetupUiActions.SavePreferenceDataUi -> {
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
                viewModelScope.launch {
                    onBoardingRepository.saveUserDetails(
                        UserDetails(
                            username = _prefScreenState.value.userName ,
                            favouriteGenres = _prefScreenState.value.genreList ,
                            selectedAvatar = _prefScreenState.value.currentSelectedAvatar
                        )
                    )
                    onBoardingRepository.updateOnBoardingStatus(status = true)
                    _uiEvents.send(UserPreferenceSetupUiEvents.NavigateToHomeScreen)
                }
            }

            is UserPreferenceSetupUiActions.MoveNext -> {
                val currentPosition = userPreferenceSetupUiActions.currentPos
                if (currentPosition < 3) {
                    _prefScreenState.value = _prefScreenState.value.copy(
                        previousPosition = currentPosition ,
                        currentPosition = currentPosition + 1
                    )
                }
            }

            is UserPreferenceSetupUiActions.MovePrevious -> {
                val currentPosition = userPreferenceSetupUiActions.currentPos
                if (currentPosition > 0) {
                    _prefScreenState.value = _prefScreenState.value.copy(
                        previousPosition = currentPosition ,
                        currentPosition = currentPosition - 1
                    )
                }
            }
        }
    }


    sealed class UserPreferenceSetupUiActions {
        data class UpdateUserNameUi(val name: String) : UserPreferenceSetupUiActions()
        data class SelectAvatar(val avatar: Int) : UserPreferenceSetupUiActions()
        data class AddSelectedGenre(val genre : String) : UserPreferenceSetupUiActions()
        data class SelectTheme(val theme : Int) : UserPreferenceSetupUiActions()
        object SavePreferenceDataUi : UserPreferenceSetupUiActions()
        data class MoveNext(val currentPos : Int) : UserPreferenceSetupUiActions()
        data class MovePrevious(val currentPos : Int) : UserPreferenceSetupUiActions()

    }

    sealed class UserPreferenceSetupUiEvents {
        object NavigateToHomeScreen : UserPreferenceSetupUiEvents()
    }

}


data class UserPrefScreenState(
    val userName: String = "",
    val currentSelectedAvatar: Int = 0,
    val genreList: List<String> = emptyList(),
    val currentSelectedTheme: Int = Theme.FOLLOW_SYSTEM.themeValue,
    val userNameFieldError : String? = null,
    val chipSelectionError : String? = null,
    val avatarSelectionError : String? = null,
    val currentPosition : Int = 0,
    val previousPosition : Int = 0
)