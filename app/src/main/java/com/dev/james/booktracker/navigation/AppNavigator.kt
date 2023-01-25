package com.dev.james.booktracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.spec.NavGraphSpec

@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier ,
    navController: NavHostController ,
    hasOnBoarded : Boolean
){
    DestinationsNavHost(
        engine = rememberNavHostEngine() ,
        navController = navController ,
        navGraph = if(hasOnBoarded) NavGraphs.root else NavGraphs.rootWithOnBoarding,
        modifier = modifier ,
        dependenciesContainerBuilder = {
            dependency(currentNavigator(hasOnBoarded))
        }
    )
}

fun DestinationScope<*>.currentNavigator(hasOnBoarded: Boolean): CoreFeatureNavigator {
    return CoreFeatureNavigator(
        navController = navController ,
        navGraph = navBackStackEntry.destination.navGraph(hasOnBoarded)
    )
}

fun NavDestination.navGraph(hasOnBoarded: Boolean): NavGraphSpec {
    hierarchy.forEach { destination ->
        if(hasOnBoarded){
            NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
                if (destination.route == navGraph.route) {
                    return navGraph
                }
            }
        }else {
            NavGraphs.rootWithOnBoarding.nestedNavGraphs.forEach { navGraph ->
                if (destination.route == navGraph.route) {
                    return navGraph
                }
            }
        }
    }

    throw RuntimeException("Unknown nav graph for destination $route")
}