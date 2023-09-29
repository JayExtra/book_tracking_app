package com.dev.james.booktracker.home.presentation.navigation

interface HomeNavigator {
    fun openHomeScreen()
    fun openReadGoalsScreen()

    fun openTrackingScreen(id : String)
}