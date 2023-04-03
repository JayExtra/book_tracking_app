package com.dev.james.on_boarding.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.on_boarding.domain.OnBoardingRepository
import com.dev.james.booktracker.on_boarding.ui.screens.UserPreferenceSetupScreen
import com.dev.james.booktracker.on_boarding.ui.viewmodel.UserPreferenceSetupViewModel
import com.dev.james.on_boarding.data.FakeOnBoardingRepository
import com.dev.james.on_boarding.ui.navigation.TestUserPreferenceScreenNavigator
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/*@UninstallModules(
    DataStoreModule::class ,
    OnBoardingModule::class ,
    DatabaseModule::class
)*/
@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferenceSetupScreenTest {

 /*   @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
*/
    @get:Rule()
    val composeTestRule = createComposeRule()

    private lateinit var repository : OnBoardingRepository
    private lateinit var viewModel : UserPreferenceSetupViewModel


    @Before
    fun setUp(){
       // hiltRule.inject()
        repository = FakeOnBoardingRepository()
        viewModel = UserPreferenceSetupViewModel(repository)
        composeTestRule.setContent {
          BookTrackerTheme {
            UserPreferenceSetupScreen(
                userSetupScreenNavigator = TestUserPreferenceScreenNavigator() ,
                userPreferenceSetupViewModel = viewModel
            )
          }
        }
    }


    @Test
    fun whenCurrentPositionStateIsZero_nameSectionComposableIsShown(){
        composeTestRule.onRoot().printToLog("TAG")
        composeTestRule.onNodeWithTag("rounded input text").assertIsDisplayed()
    }

    @Test
    fun whenCurrentPositionStateIsZero_nextButtonIsShown(){
        composeTestRule.onNodeWithTag("rounded outlined button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("rounded button text" , useUnmergedTree = true)
            .assertTextEquals("next")
    }


    @Test
    fun whenNextButtonIsClicked_updatesCurrentPosTo1_previousButtonShown(){
       composeTestRule.onNodeWithTag("rounded outlined button").performClick()
        val currentPos = viewModel.prefScreenState.value.currentPosition
        assertThat(currentPos).isEqualTo(1)
        composeTestRule.onAllNodesWithTag("rounded button text" , useUnmergedTree = true)
            .filterToOne(hasText("previous"))
            .assertIsDisplayed()
    }

    @Test
    fun whenNextButtonIsClicked_nameSectionDisappears_avatarSectionAppears(){
        composeTestRule.onNodeWithTag("rounded outlined button").performClick()
        composeTestRule.onNodeWithTag("avatar section title").assertIsDisplayed()
        composeTestRule.onNodeWithTag("avatar grid").assertIsDisplayed()
    }

    @Test
    fun whenAvatarItemIsClicked_avatarIsSelected(){
        composeTestRule.onNodeWithTag("rounded outlined button").performClick()
        composeTestRule.onNodeWithTag("avatar grid").onChildAt(1)
            .performClick()

        val selectedAvatar = viewModel.prefScreenState.value.currentSelectedAvatar
        assertThat(selectedAvatar).isGreaterThan(0)
    }



}