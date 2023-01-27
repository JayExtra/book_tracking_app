package com.dev.james.booktracker.compose_ui.ui.theme

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Orange,
    primaryVariant = OrangeLight  ,
    secondary = Orange ,
    onPrimary = White ,
    onSecondary = White ,
    error = Color.Red
)

private val LightColorPalette = lightColors(
    primary = Brown,
    primaryVariant = BrownLight,
    secondary = Orange,
//     Other default colors to override
    background = White,
//    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//
)

@Composable
fun BookTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    if(darkTheme){
      systemUiController.setStatusBarColor(
          color = White,
          darkIcons = true
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




