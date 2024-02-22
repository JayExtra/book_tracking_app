package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
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

@Composable
fun BottomCardDecorationComponent() {


}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun LineArch1() {
    Canvas(
        modifier = Modifier.size(300.dp),
        contentDescription = ""
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        /*drawLine(
            start = Offset(x = canvasWidth / 2, y = 0f),
            end = Offset(x = canvasWidth / 2, y = canvasHeight),
            color = Color.Black ,
            strokeWidth = 60f
        )*/

        drawArc(
            size = Size(width = 250f , height = 250f) ,
            color = Color.Black ,
            useCenter = false ,
            startAngle = 90f ,
            sweepAngle = 180f ,
            style = Stroke(20.dp.toPx() , cap =  StrokeCap.Round)
        )
    }
}