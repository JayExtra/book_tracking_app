package com.example.core_navigation.navigation

import com.dev.james.achievements.presentation.ui.screens.destinations.AchievementsScreenDestination
import com.dev.james.book_tracking.presentation.ui.screens.destinations.TrackBookScreenDestination
import com.dev.james.booktracker.home.presentation.screens.destinations.HomeScreenDestination
import com.dev.james.booktracker.home.presentation.screens.destinations.ReadGoalScreenDestination
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.OnBoardingWelcomeScreenDestination
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.UserPreferenceSetupScreenDestination
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.WelcomeHelloMessageScreenDestination
import com.dev.james.my_library.presentation.ui.destinations.MyLibraryScreenDestination
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

object NavGraphs {

    val onBoarding = object : NavGraphSpec {

        override val route: String = "on-boarding"

        override val startRoute: Route = OnBoardingWelcomeScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            OnBoardingWelcomeScreenDestination ,
            HomeScreenDestination ,
            WelcomeHelloMessageScreenDestination ,
            UserPreferenceSetupScreenDestination ,
            AchievementsScreenDestination ,
            MyLibraryScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val home = object  : NavGraphSpec {

        override val route: String =
            "home"

        override val startRoute: Route =
            HomeScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            HomeScreenDestination ,
            OnBoardingWelcomeScreenDestination ,
            ReadGoalScreenDestination ,
            TrackBookScreenDestination
        ).routedIn(this)
            .associateBy { it.route }

    }

    val myLibrary = object : NavGraphSpec {

        override val route: String =
            "my-library"

        override val startRoute: Route =
            MyLibraryScreenDestination routedIn this

        override val destinationsByRoute =
            listOf<DestinationSpec<*>>(
                MyLibraryScreenDestination
            ).routedIn(this)
                .associateBy { it.route }

    }

    val achievements = object : NavGraphSpec {

        override val route: String =
            "achievements"

        override val startRoute: Route =
            AchievementsScreenDestination routedIn this

        override val destinationsByRoute =
            listOf<DestinationSpec<*>>(
                AchievementsScreenDestination
            ).routedIn(this)
                .associateBy { it.route }

    }

    val bookTracking = object : NavGraphSpec {

        override val route: String
            = "book-tracking"
        override val startRoute: Route
            = TrackBookScreenDestination routedIn this
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            AchievementsScreenDestination ,
            HomeScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val rootWithOnBoarding = object : NavGraphSpec {
        override val route: String = "rootWithOnBoarding"
        override val startRoute: Route
             = onBoarding
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()

        override val nestedNavGraphs = listOf(
            onBoarding ,
            home ,
            myLibrary ,
            achievements ,
            bookTracking
        )
    }

    val root = object : NavGraphSpec {
        override val route: String =
            "root"
        override val startRoute: Route =
            home
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(
            onBoarding,
            home ,
            myLibrary ,
            achievements ,
            bookTracking
        )
    }
}