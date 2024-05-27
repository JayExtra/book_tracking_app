package com.dev.james.my_library.presentation.navigation


interface MyBookShelfScreenNavigator {
    fun popBackStack()
    fun navToAddBookScreen()

    fun fromBookShelfToTrackBookScreen(bookId : String )
}