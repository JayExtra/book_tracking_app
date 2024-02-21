package com.dev.james.my_library.presentation.navigation

import com.dev.james.booktracker.compose_ui.ui.enums.PreviousScreenDestinations

interface MyLibraryScreenNavigator {
    fun openHomeScreenDestination()

    fun openAddBookScreenDestination()

    fun openBookTrackingScreenDestination(bookId : String , currentDestination: PreviousScreenDestinations)
}