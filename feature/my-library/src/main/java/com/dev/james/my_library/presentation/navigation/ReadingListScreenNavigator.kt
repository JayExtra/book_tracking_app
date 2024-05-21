package com.dev.james.my_library.presentation.navigation

interface ReadingListScreenNavigator {
    fun navBackToLibraryScreen()
    fun navToBookDetailsScreen(bookId : String)

}