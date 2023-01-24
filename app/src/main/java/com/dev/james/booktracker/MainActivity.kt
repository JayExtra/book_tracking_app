package com.dev.james.booktracker

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.home.presentation.screens.HomeScreen
import com.dev.james.booktracker.navigation.AppNavigation
import com.dev.james.booktracker.on_boarding.ui.screens.OnBoardingWelcomeScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel : MainViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition(condition = {
                mainViewModel.isLoading.value
            })
        }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
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
        }
        setContent {
            BookTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   val navController = rememberNavController()
                    val newBackStackEntry by navController.currentBackStackEntryAsState()
                    val route = newBackStackEntry?.destination?.route
                    val hasOnBoarded = mainViewModel.isOnBoarded.collectAsStateWithLifecycle()
                    AppNavigation(
                        navController = navController,
                        hasOnBoarded = hasOnBoarded.value ,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
