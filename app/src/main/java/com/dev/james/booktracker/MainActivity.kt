package com.dev.james.booktracker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.james.achievements.presentation.ui.screens.destinations.AchievementsScreenDestination
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.home.presentation.screens.destinations.HomeScreenDestination
import com.dev.james.booktracker.ui.components.StandardScaffold
import com.dev.james.my_library.presentation.ui.destinations.MyLibraryScreenDestination
import com.example.core_navigation.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel : MainViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition(condition = {
                mainViewModel.isLoading.value
            })
        }
        /*splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Create your custom animation.
            val slideLeft = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_X,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideLeft.interpolator = AnticipateInterpolator()
            slideLeft.duration = 200L

            // Call SplashScreenView.remove at the end of your custom animation.
            slideLeft.doOnEnd { splashScreenView.remove() }

            // Run your animation.
            slideLeft.start()
        }*/

        setContent {

            val theme by mainViewModel.theme.collectAsStateWithLifecycle(
                initialValue = Theme.FOLLOW_SYSTEM.themeValue ,
                context = Dispatchers.Main.immediate
            )

            val hasOnBoarded by mainViewModel.isOnBoarded.collectAsStateWithLifecycle(
                initialValue = false ,
                context = Dispatchers.Main.immediate
            )

            BookTrackerTheme(
                theme = theme
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val newBackStackEntry by navController.currentBackStackEntryAsState()
                    val route = newBackStackEntry?.destination?.route


                    StandardScaffold(
                        navController = navController ,
                        showBottomBar = route in listOf(
                            "home/${HomeScreenDestination.route}" ,
                            "my-library/${MyLibraryScreenDestination.route}" ,
                            "achievements/${AchievementsScreenDestination.route}"
                        ) ,
                        hasOnBoarded = hasOnBoarded
                    ) { innerPadding ->

                        Box(modifier = Modifier.padding(innerPadding)){
                            AppNavigation(
                                navController = navController,
                                hasOnBoarded = hasOnBoarded ,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }

                    }

                }
            }
        }
    }
}
