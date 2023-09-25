package com.example.core_navigation.navigation

import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.dev.james.achievements.presentation.navigation.AchievementsScreenNavigator
import com.dev.james.book_tracking.presentation.ui.navigation.BookTrackNavigation
import com.dev.james.book_tracking.presentation.ui.screens.destinations.TrackBookScreenDestination
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.dev.james.booktracker.home.presentation.screens.destinations.HomeScreenDestination
import com.dev.james.booktracker.home.presentation.screens.destinations.ReadGoalScreenDestination
import com.dev.james.booktracker.on_boarding.ui.navigation.HelloMessageNavigator
import com.dev.james.booktracker.on_boarding.ui.navigation.OnBoardingNavigator
import com.dev.james.booktracker.on_boarding.ui.navigation.UserSetupScreenNavigator
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.OnBoardingWelcomeScreenDestination
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.UserPreferenceSetupScreenDestination
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.WelcomeHelloMessageScreenDestination
import com.dev.james.my_library.presentation.navigation.MyLibraryScreenNavigator
import com.example.core_navigation.navigation.NavGraphs
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.NavGraphSpec

class CoreFeatureNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController
) : HomeNavigator ,
    OnBoardingNavigator ,
    HelloMessageNavigator ,
    UserSetupScreenNavigator ,
        AchievementsScreenNavigator ,
        MyLibraryScreenNavigator ,
        BookTrackNavigation
{

    override fun openOnBoardingWelcomeScreen() {
        navController.navigate(
            OnBoardingWelcomeScreenDestination within navGraph
        )
    }

    override fun openWelcomeMessageScreen() {
        navController.navigate(
            WelcomeHelloMessageScreenDestination within navGraph
        )
    }


    override fun openHome() {
        navController.navigate(direction = HomeScreenDestination within navGraph){
            popUpTo(NavGraphs.rootWithOnBoarding){
                inclusive = true
            }
        }
    }

    override fun openHomeScreen() {
        navController.navigate(HomeScreenDestination within navGraph){
            popUpTo(NavGraphs.root){
                inclusive = true
            }
        }
    }

    override fun openReadGoalsScreen() {
        navController.navigate(
            ReadGoalScreenDestination within navGraph
        )
    }

    override fun openTrackingScreen() {
        navController.navigate(
            TrackBookScreenDestination within navGraph
        )
    }

    override fun navigateToUserDetailsCaptureScreen() {
        navController.navigate(
            UserPreferenceSetupScreenDestination within navGraph
        ){
            popUpTo(NavGraphs.rootWithOnBoarding){
                inclusive = true
            }
        }
    }

    override fun navigateToHomeScreen() {
        navController.navigate(direction = HomeScreenDestination within navGraph){
            popUpTo(NavGraphs.rootWithOnBoarding){
                inclusive = true
            }
        }
    }


    override fun goToHomeDestination() {
        navController.navigate(HomeScreenDestination within navGraph)
    }

    override fun openHomeScreenDestination() {
        navController.navigate(HomeScreenDestination within navGraph)
    }

    override fun backToHomeScreen() {
        navController.navigate(
            HomeScreenDestination within navGraph ,
        ){
            popUpTo(NavGraphs.root){
                inclusive = true
            }
        }
    }

    override fun openPdfScreen() {
        //will open pdf screen
    }

}