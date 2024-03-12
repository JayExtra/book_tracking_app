package com.dev.james.booktracker.home.presentation.navigation

import com.dev.james.booktracker.compose_ui.ui.enums.PreviousScreenDestinations

interface HomeNavigator {
    fun openHomeScreen()
    fun openReadGoalsScreen(goalId : String?)

    fun openTrackingScreen(id : String , currentDestination : PreviousScreenDestinations)

    fun openMyLibraryScreen()

    fun openAddBookScreen(previousDestination : PreviousScreenDestinations)

    //add navigation to add book screen
}