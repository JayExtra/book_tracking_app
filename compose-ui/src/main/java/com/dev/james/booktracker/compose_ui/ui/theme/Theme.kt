package com.dev.james.booktracker.compose_ui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Orange,
    primaryVariant = OrangeLight  ,
    secondary = OrangeDark ,
    onPrimary = Color.White ,
    secondaryVariant = OrangeLight,
    onSecondary = Color.White ,
    error = Color.Red
)

private val LightColorPalette = lightColors(
    primary = Brown,
    primaryVariant = BrownLight,
    secondary = Orange
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BookTrackerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = BookAppTypography,
        shapes = BookAppShapes,
        content = content
    )
}