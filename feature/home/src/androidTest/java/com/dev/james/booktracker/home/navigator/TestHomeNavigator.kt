package com.dev.james.booktracker.home.navigator

import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import timber.log.Timber

class TestHomeNavigator : HomeNavigator {
    override fun openHomeScreen() {
        Timber.d("Opening home screen from home navigator")
    }

    override fun openReadGoalsScreen() {
        Timber.d("Opening read goals screen from home navigator ")
    }
}