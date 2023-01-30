package com.dev.james.booktracker.compose_ui.ui.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Orange,
    onPrimary = PrimaryTextColor ,
    secondary = OrangeLight ,
    onSecondary = SecondaryTextColor ,
    error = Color.Red,
    background = BackgroundDarkColor ,
    onBackground = Color.White ,
    surface = SurfaceDark ,
    onSurface = Color.White ,
    secondaryVariant = Color.White ,
    onError = OnErrorColor
)

private val LightColorPalette = lightColors(
    primary = Brown,
    primaryVariant = BrownLight,
    secondary = Orange,
    onSecondary = PrimaryTextColor,
//     Other default colors to override
    background = BackgroundLightColor,
    onBackground = Color.Black,
    surface = SurfaceLight,
    onPrimary = SecondaryTextColor,
    onSurface = Color.Black ,
    error = ErrorColor ,
    onError = OnErrorColor
//
)

@Composable
fun BookTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    if(darkTheme){
      systemUiController.setStatusBarColor(
          color = Black,
          darkIcons = false
      )
    }else{
        systemUiController.setStatusBarColor(
            color = White ,
            darkIcons = true
        )
    }

    MaterialTheme(
        colors = if(darkTheme) DarkColorPalette else LightColorPalette,
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





