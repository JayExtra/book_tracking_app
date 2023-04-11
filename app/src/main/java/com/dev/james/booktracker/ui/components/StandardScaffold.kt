package com.dev.james.booktracker.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder
import com.example.core_navigation.navigation.BottomNavItem
import com.example.core_navigation.navigation.NavGraphs
import com.example.core_navigation.navigation.navGraph
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardScaffold(
    navController : NavController ,
    showBottomBar : Boolean = true ,
    hasOnBoarded : Boolean = true ,
    navItems : List<BottomNavItem> = listOf(
        BottomNavItem.Home ,
        BottomNavItem.MyLibrary ,
        BottomNavItem.Achievements
    ) ,
    content : @Composable (paddingValues : PaddingValues) -> Unit
){

    Scaffold(
        bottomBar = {
              if(showBottomBar) {
                  val currentSelectedItem by navController.currentScreenAsState(hasOnBoarded)
                  NavigationBar(
                      containerColor = MaterialTheme.colorScheme.background,
                      tonalElevation = 3.dp

                  ) {
                    navItems.forEach { item ->
                       NavigationBarItem(
                           icon = {
                                  Icon(
                                      painter = painterResource(id = item.icon),
                                      contentDescription = item.title ,
                                      tint = if(currentSelectedItem == item.screen){
                                          MaterialTheme.colorScheme.primary
                                      }else{
                                          MaterialTheme.colorScheme.onSurfaceVariant
                                      }
                                  )

                           } ,
                           label = {
                               Text(
                                   text = item.title,
                                   fontSize = 9.sp,
                                   color = if (currentSelectedItem == item.screen) {
                                       MaterialTheme.colorScheme.primary
                                   } else {
                                       MaterialTheme.colorScheme.onSurfaceVariant
                                   },
                                   fontWeight = if (currentSelectedItem == item.screen) {
                                       FontWeight.ExtraBold
                                   } else {
                                       FontWeight.Normal
                                   }
                               )
                           },
                           alwaysShowLabel = true,
                           selected = currentSelectedItem == item.screen ,
                           onClick = {
                                navController.navigate(
                                    item.screen , fun NavOptionsBuilder.(){
                                        launchSingleTop = true
                                        restoreState = true

                                        popUpTo(navController.graph.findStartDestination().id){
                                            saveState = true
                                        }
                                    }
                                )

                           })
                    }
                  }
              }

        }
    ) { paddingValues ->
          content(paddingValues)
    }

}


/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
fun NavController.currentScreenAsState(
    hasOnBoarded: Boolean
): State<NavGraphSpec> {

    val selectedItem = remember { mutableStateOf(NavGraphs.home) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            selectedItem.value = destination.navGraph(hasOnBoarded)
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}