package com.dev.james.booktracker.compose_ui.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.R


//app main font family
private val Montserrat = FontFamily(
    Font(R.font.montserrat_light) ,
    Font(R.font.montserrat_regular) ,
    Font(R.font.montserrat_medium , weight = FontWeight.W500),
    Font(R.font.montserrat_bold , weight = FontWeight.W600)
)
// Set of Material typography styles to start with
val BookAppTypography = Typography(
    h4 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.W600 ,
        fontSize = 30.sp
    ),
    h5 = TextStyle(
        fontFamily = Montserrat ,
        fontWeight = FontWeight.W600 ,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Montserrat ,
        fontWeight = FontWeight.W600 ,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

