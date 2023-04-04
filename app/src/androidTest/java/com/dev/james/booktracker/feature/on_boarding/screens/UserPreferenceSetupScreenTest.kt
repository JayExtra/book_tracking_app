package com.dev.james.booktracker.feature.on_boarding.screens

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.dev.james.booktracker.MainActivity
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.core_database.di.DatabaseModule
import com.dev.james.booktracker.core_datastore.di.DataStoreModule
import com.dev.james.booktracker.on_boarding.di.OnBoardingModule
import com.dev.james.booktracker.on_boarding.ui.screens.UserPreferenceSetupScreen
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.UserPreferenceSetupScreenDestination
import com.dev.james.booktracker.on_boarding.ui.viewmodel.UserPreferenceSetupViewModel
import com.example.core_navigation.navigation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule


@HiltAndroidTest
@UninstallModules(
    DataStoreModule::class ,
    OnBoardingModule::class ,
    DatabaseModule::class
)
class UserPreferenceSetupScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp(){
        hiltRule.inject()
        composeTestRule.setContent {
            val navController = rememberNavController()
            BookTrackerTheme {
              DestinationsNavHost(
                  navGraph = NavGraphs.rootWithOnBoarding ,
                  navController = navController ,
                  engine = rememberNavHostEngine() ,
                  startRoute = UserPreferenceSetupScreenDestination
              )
            }
        }
    }

}