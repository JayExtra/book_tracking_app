package com.dev.james.booktracker.navigation

import com.dev.james.booktracker.home.presentation.screens.destinations.HomeScreenDestination
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.OnBoardingWelcomeScreenDestination
import com.dev.james.booktracker.on_boarding.ui.screens.destinations.WelcomeHelloMessageScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
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
            WelcomeHelloMessageScreenDestination
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
            OnBoardingWelcomeScreenDestination
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
            home
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
            home
        )
    }
}