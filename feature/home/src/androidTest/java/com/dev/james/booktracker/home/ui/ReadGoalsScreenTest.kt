package com.dev.james.booktracker.home.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.dev.james.booktracker.home.data.repository.FakeBooksRepository
import com.dev.james.booktracker.home.data.repository.FakeGoalsRepository
import com.dev.james.booktracker.home.domain.repositories.BooksRepository
import com.dev.james.booktracker.home.domain.repositories.GoalsRepository
import com.dev.james.booktracker.home.navigator.TestHomeNavigator
import com.dev.james.booktracker.home.presentation.screens.ReadGoalScreen
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class ReadGoalsScreenTest {
    @get:Rule()
    val composeTestRule = createComposeRule()

    private lateinit var readGoalScreenViewModel : ReadGoalsScreenViewModel
    private lateinit var booksRepository: BooksRepository
    private lateinit var goalsRepository: GoalsRepository
    @Before
    fun setUp(){
        booksRepository = FakeBooksRepository()
        goalsRepository = FakeGoalsRepository()

        readGoalScreenViewModel = ReadGoalsScreenViewModel(
            booksRepository, goalsRepository
        )
        composeTestRule.setContent {
         ReadGoalScreen(
             homeNavigator = TestHomeNavigator() ,
             readGoalsScreenViewModel = readGoalScreenViewModel
         )
        }
    }

    @Test
    fun open_readGoalsScreen_bottomSheetScaffold_appears(){
        composeTestRule.onNodeWithTag("read_goals_screen_scaffold")
            .assertIsDisplayed()
    }

}