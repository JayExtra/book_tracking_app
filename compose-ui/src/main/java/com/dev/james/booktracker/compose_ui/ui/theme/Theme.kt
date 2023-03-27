package com.dev.james.booktracker.compose_ui.ui.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = Orange60,
    onPrimary = Orange30 ,
    primaryContainer = Orange40 ,
    onPrimaryContainer= Orange70 ,
    inversePrimary = Orange50 ,
    secondary = DarkOrange45 ,
    onSecondary = DarkOrange20 ,
    secondaryContainer =  DarkOrange30,
    onSecondaryContainer = DarkOrange25,
    error = ErrorColor60,
    background = SurfaceDark23 ,
    onBackground = SurfaceDark80 ,
    surface = Brown60 ,
    onSurface = Brown80 ,
    onError = ErrorColor70 ,
    surfaceVariant = Brown60 ,
    onSurfaceVariant = Brown80
)

private val LightColorPalette = lightColorScheme(
    primary = Brown30,
    onPrimary = Brown50,
    primaryContainer = Brown40 ,
    onPrimaryContainer= Brown60 ,
    secondary = Brown20,
    onSecondary = Brown70,
    inversePrimary = Brown40 ,
//     Other default colors to override
    background = SurfaceLight ,
    onBackground = SurfaceDark70 ,
    surface = Brown60 ,
    onSurface = Brown80 ,
    error = ErrorColor60,
    onError = ErrorColor70 ,
    surfaceVariant = Brown60 ,
    onSurfaceVariant = Brown80
//
)

@Composable
fun BookTrackerTheme(
    theme : Int,
    content: @Composable () -> Unit) {
    val autoColors = if(isSystemInDarkTheme()) DarkColorPalette else LightColorPalette

    val dynamicColors = if(supportsDynamicTheming()) {
        val context = LocalContext.current
        if(isSystemInDarkTheme()){
            dynamicDarkColorScheme(context)
        }else {
            dynamicLightColorScheme(context)
        }
    } else {
        autoColors
    }

    val colors = when(theme) {
        Theme.LIGHT_THEME.themeValue -> LightColorPalette
        Theme.DARK_THEME.themeValue -> DarkColorPalette
        Theme.MATERIAL_YOU.themeValue -> dynamicColors
        else -> autoColors
    }

    val systemUiController = rememberSystemUiController()
    if(isSystemInDarkTheme()){
        SideEffect {
            systemUiController.setStatusBarColor(
                color = Black,
                darkIcons = false
            )
        }
    }else{
        SideEffect {
            systemUiController.setStatusBarColor(
                color = White ,
                darkIcons = true
            )
        }
    }

    MaterialTheme(
        colorScheme = colors ,
        typography = BookAppTypography,
        shapes = BookAppShapes,
        content = content
    )
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
private fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

// To be used to set the preferred theme inside settings
enum class Theme(
    val themeValue: Int
) {
    MATERIAL_YOU(
        themeValue = 12
    ),
    FOLLOW_SYSTEM(
        themeValue = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    ),
    LIGHT_THEME(
        themeValue = AppCompatDelegate.MODE_NIGHT_NO
    ),
    DARK_THEME(
        themeValue = AppCompatDelegate.MODE_NIGHT_YES
    );
}





