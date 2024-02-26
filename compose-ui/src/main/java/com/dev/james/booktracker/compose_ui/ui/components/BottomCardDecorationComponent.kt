package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.enums.DefaultColors
import com.dev.james.booktracker.compose_ui.ui.theme.BackgroundDarkColor
import com.dev.james.booktracker.compose_ui.ui.theme.BackgroundLightColor


@OptIn(ExperimentalFoundationApi::class)
@Preview()
@Composable
fun BottomCardDecorationComponent(
    modifier: Modifier = Modifier ,
    color: Long = DefaultColors.CARD_DECORATOR_DEFAULT_COLOR
) {
    Canvas(
        modifier = modifier.background(color = Color.Transparent),
        contentDescription = ""
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val arcRadius = 350f
        val arcRadiusSmall = 250f

        drawArc(
            size = Size(width = 350f , height = 350f) ,
            color = Color(color),
            useCenter = false ,
            startAngle = 90f ,
            sweepAngle = 180f ,
            style = Stroke(20.dp.toPx() , cap = StrokeCap.Round) ,
            topLeft = Offset(
                (canvasWidth / 2) - (arcRadius / 2),
                canvasHeight / 2 - (arcRadius / 2)
            )
        )

        drawCircle(
            color = Color(color),
            radius = 60f
        )

        drawArc(
            size = Size(width = 250f , height = 250f) ,
            color = Color(color) ,
            useCenter = false ,
            startAngle = -90f ,
            sweepAngle = 180f ,
            style = Stroke(15.dp.toPx() ,  cap = StrokeCap.Round) ,
            topLeft = Offset(
                (canvasWidth / 2) - (arcRadiusSmall / 3),
                canvasHeight / 2 - (arcRadiusSmall / 2)
            )
        )
    }
}