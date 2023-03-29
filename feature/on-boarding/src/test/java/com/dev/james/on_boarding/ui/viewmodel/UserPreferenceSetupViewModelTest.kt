package com.dev.james.on_boarding.ui.viewmodel

import com.dev.james.booktracker.on_boarding.domain.OnBoardingRepository
import com.dev.james.booktracker.on_boarding.ui.viewmodel.UserPreferenceSetupViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferenceSetupViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var repository : OnBoardingRepository
    private lateinit var viewModel : UserPreferenceSetupViewModel
    @Before
    fun setUp(){
        repository = Mockito.mock(OnBoardingRepository::class.java)
        viewModel = UserPreferenceSetupViewModel(repository)
    }


    @Test
    fun `changing name field text action , from ui , updates ui state with name field text`() = runTest {
        //given
        val action = UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
            .UpdateUserNameUi(name = "Test Name")

        //when
        viewModel.setUserPreference(
            action
        )

        //then
        val uiEvent = viewModel.prefScreenState.value

        assertThat(uiEvent.userName).isEqualTo("Test Name")

    }

    @Test
    fun `changing name field text action using blank name from ui updates ui state with name field error text`() = runTest {
        //given
        val action = UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
            .UpdateUserNameUi(name = "")

        //when
        viewModel.setUserPreference(
            action
        )

        //then
        val uiEvent = viewModel.prefScreenState.value

        assertThat(uiEvent.userNameFieldError).isNotNull()

    }

    @Test
    fun `selecting avatar in ui passes avatar image resource id and updates ui state with selected avatar id` () = runTest {
        val action = UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
            .SelectAvatar(avatar = 12)

        viewModel.setUserPreference(
            action
        )

        val uiState = viewModel.prefScreenState.value

        assertThat(uiState.currentSelectedAvatar).isEqualTo(12)
    }

    @Test
    fun `selecting genre chip in ui passes genre to genre selection action , updates ui state with list of selected genre`() = runTest{
        val selectedGenre = "Horror"
        val action = UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
            .AddSelectedGenre(genre = selectedGenre)

        viewModel.setUserPreference(
            action
        )

        val uiState = viewModel.prefScreenState.value

        assertThat(uiState.genreList).contains(selectedGenre)
    }

    @Test
    fun `selecting an already added genre in ui removes genre from selected genres list`() = runTest {
        val selectedGenre = "Horror"
        val action1 = UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
            .AddSelectedGenre(genre = selectedGenre)

        viewModel.setUserPreference(action1)

        val action2 = UserPreferenceSetupViewModel.UserPreferenceSetupUiActions.AddSelectedGenre(genre = selectedGenre)

        viewModel.setUserPreference(action2)

        val uiState = viewModel.prefScreenState.value

        assertThat(uiState.genreList).doesNotContain(selectedGenre)

    }

    @Test
    fun `selecting more than 5 genres in ui , sends more than 5 selected genres warning message to ui `() = runTest {
        val selectedGenreList = listOf("Horror" , "Action" , "Crime" , "Romance" , "Adventure")

        selectedGenreList.forEach { genre ->
            val action = UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
                .AddSelectedGenre(genre = genre)
            viewModel.setUserPreference(action)
        }

        val sixthGenre = "Erotic"
        val sixthGenreAction = UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
            .AddSelectedGenre(genre = sixthGenre)
        viewModel.setUserPreference(sixthGenreAction)

        val uiState = viewModel.prefScreenState.value

        assertThat(uiState.chipSelectionError).isNotNull()

    }

    @Test
    fun `selecting desired theme in ui updates the ui state to contain the desired ui theme` () = runTest {
        val selectedTheme = 1

        val action = UserPreferenceSetupViewModel
            .UserPreferenceSetupUiActions
            .SelectTheme(theme = selectedTheme)

        viewModel.setUserPreference(action)

        val uiState = viewModel.prefScreenState.value

        assertThat(uiState.currentSelectedTheme).isEqualTo(selectedTheme)
    }

    @Test
    fun `pressing the next button in ui updates current position uiState`() = runTest {
        val currentPos = 0

        val action = UserPreferenceSetupViewModel
            .UserPreferenceSetupUiActions
            .MoveNext(currentPos = currentPos)

        viewModel.setUserPreference(action)

        val uiState = viewModel.prefScreenState.value

        assertThat(uiState.currentPosition).isGreaterThan(currentPos)
    }

    @Test
    fun `pressing the previous button in ui updates current position uiState`() = runTest {
        val currentPos = 1
        val action = UserPreferenceSetupViewModel
            .UserPreferenceSetupUiActions
            .MovePrevious(currentPos = currentPos)

        viewModel.setUserPreference(action)

        val uiState = viewModel.prefScreenState.value

        assertThat(uiState.currentPosition).isLessThan(currentPos)

    }

}