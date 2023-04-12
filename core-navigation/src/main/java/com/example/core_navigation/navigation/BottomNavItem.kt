package com.example.core_navigation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.dev.james.booktracker.core.R
import com.ramcosta.composedestinations.spec.NavGraphSpec

sealed class BottomNavItem(
    var title: String,
    var icon: Int,
    var screen: NavGraphSpec
) {
    object Home : BottomNavItem(
        title = "Home" ,
        icon = R.drawable.home_icon_24 ,
        screen = NavGraphs.home
    )

    object MyLibrary : BottomNavItem(
        title = "My Library" ,
        icon = R.drawable.library_icon_24 ,
        screen = NavGraphs.myLibrary
    )

    object Achievements : BottomNavItem(
        title = "Achievements" ,
        icon = R.drawable.trophy_icon_24 ,
        screen = NavGraphs.achievements
    )
}
