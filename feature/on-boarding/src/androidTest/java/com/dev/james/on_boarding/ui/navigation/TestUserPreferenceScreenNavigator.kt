package com.dev.james.on_boarding.ui.navigation

import com.dev.james.booktracker.on_boarding.ui.navigation.UserSetupScreenNavigator

class TestUserPreferenceScreenNavigator : UserSetupScreenNavigator {
    override fun navigateToHomeScreen() {
        println("Navigation to home screen invoked")
    }
}