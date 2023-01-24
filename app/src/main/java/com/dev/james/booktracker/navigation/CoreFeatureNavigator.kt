package com.dev.james.booktracker.navigation

import androidx.navigation.NavController
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.dev.james.booktracker.home.presentation.screens.destinations.HomeScreenDestination
import com.dev.james.booktracker.on_boarding.ui.navigation.OnBoardingNavigator
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.OnBoardingWelcomeScreenDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import timber.log.Timber

class CoreFeatureNavigator(
    private val navGraph: NavGraphSpec ,
    private val navController: NavController
) : HomeNavigator , OnBoardingNavigator {

    override fun openOnBoardingWelcomeScreen() {
        navController.navigate(
            OnBoardingWelcomeScreenDestination within navGraph
        )
    }

    override fun openWelcomeMessageScreen() {
        Timber.d("welcome screen with message")
    }

    override fun openOnBoardingPreferenceSetupScreen() {
        Timber.d("On boarding preference setup screens")
    }

    override fun openHome() {
        navController.navigate(HomeScreenDestination within navGraph)
    }

    override fun openHomeScreen() {
        navController.navigate(HomeScreenDestination within navGraph)
    }

}