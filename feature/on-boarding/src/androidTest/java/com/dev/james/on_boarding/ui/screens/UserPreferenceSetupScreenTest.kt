package com.dev.james.on_boarding.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.on_boarding.domain.OnBoardingRepository
import com.dev.james.booktracker.on_boarding.ui.screens.UserPreferenceSetupScreen
import com.dev.james.booktracker.on_boarding.ui.viewmodel.UserPrefScreenState
import com.dev.james.booktracker.on_boarding.ui.viewmodel.UserPreferenceSetupViewModel
import com.dev.james.on_boarding.data.FakeOnBoardingRepository
import com.dev.james.on_boarding.ui.navigation.TestUserPreferenceScreenNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

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


}