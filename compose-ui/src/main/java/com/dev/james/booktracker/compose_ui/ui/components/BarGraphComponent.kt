package com.dev.james.booktracker.compose_ui.ui.components
import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.dev.james.booktracker.compose_ui.ui.enums.BarType
import kotlin.math.round
@Composable
@Preview(showBackground = true)
fun BarGraph(
    graphBarData2 : Map<String , Float> = mapOf(
        "Sun" to 0.33333f , "Mon" to 0.55879f , "Tue" to 0.6666667f , "Wen" to 0.555556f
        , "Thur" to 0.777778f , "Fri" to 0.445678f , "Sat" to 0.3221567f
    ),
    graphBarData: List<Float> = listOf(0.33333334f, 0.6666667f, 0.55879f, 0.5555556f, 0.7777778f),
    xAxisScaleData: List<Int> = listOf( 2 , 3 ,4 ,5, 6),
    targetTime : Int = 2 ,
    barData_:List<Int> = listOf(0 , 1 , 2),
    height: Dp = 380.dp,
    roundType: BarType = BarType.CIRCULAR_TYPE,
    barWidth: Dp = 38.dp,
    barColor: Color = MaterialTheme.colorScheme.primary,
    barArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly
) {

    val barData by remember {
        mutableStateOf(barData_+0)
    }
    val _targetTime by remember {
        mutableStateOf(targetTime + 0)
    }

    // for getting screen width and height you can use LocalConfiguration
    val configuration = LocalConfiguration.current
    // getting screen width
    val width = configuration.screenWidthDp.dp

    // bottom height of the X-Axis Scale
    //controls generally the height of the dotted x-axis
    val xAxisScaleHeight = 80.dp

    //this moves the entire dotted line x-axis value either up or down
    val yAxisScaleSpacing by remember {
        mutableStateOf(100f)
    }

    //controls the space between the bar lines especially the y-axis values
    val yAxisTextWidth by remember {
        mutableStateOf(80.dp)
    }

    // bar shape
    val barShape =
        when (roundType) {
            BarType.CIRCULAR_TYPE -> CircleShape
            BarType.TOP_CURVED -> RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
        }

    val density = LocalDensity.current
    // y-axis scale text paint
    val textPaint = remember(density) {
        Paint().apply {
            color = Color.Black.hashCode()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    // for y coordinates of y-axis scale to create horizontal dotted line indicating y-axis scale
    val yCoordinates = mutableListOf<Float>()
    // for dotted line effect
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    // height of vertical line over x-axis scale connecting x-axis horizontal line
    val lineHeightXAxis = 10.dp
    // height of horizontal line over x-axis scale
    val horizontalLineHeight = 5.dp

    //main box that stacks both the dotted y axis and the graph itself
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        // y-axis scale and horizontal dotted lines on graph indicating y-axis scale
        //the dotted line y-axis values
        Column(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 3.dp)
                .height(height)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {

            Canvas(modifier = Modifier.padding(bottom = 26.dp).fillMaxSize()) {

                // Y-Axis Scale Text
                (0..2).forEach { i ->
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${targetTime * i / 2} h",
                            50f,
                            size.height - yAxisScaleSpacing - i * size.height / 3f,
                            textPaint
                        )
                    }
                    yCoordinates.add(size.height - yAxisScaleSpacing - i * size.height / 3f)
                }

                // horizontal dotted lines on graph indicating y-axis scale
                (1..2).forEach {
                    drawLine(
                        start = Offset(x = yAxisScaleSpacing +30f, y = yCoordinates[it]),
                        end = Offset(x= size.width, y = yCoordinates[it]),
                        color = Color.Gray,
                        strokeWidth = 5f,
                        pathEffect = pathEffect
                    )
                }

            }

        }

        // Graph with Bar Graph and X-Axis Scale
        // main graph
        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(width - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.BottomCenter
        ) {

            //row to show each graph bar
            Row(
                modifier = Modifier
                    .width(width - yAxisTextWidth),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ) {
                for((key , value) in graphBarData2){

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Top,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        // Each Graph
                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .clip(barShape)
                                .width(barWidth)
                                .height(height - 10.dp)
                                .background(Color.Transparent),
                            contentAlignment = BottomCenter
                        ) {

                            val constraints = ConstraintSet {
                                val graphBar = createRefFor("graph_bar")
                                val graphValue = createRefFor("graph_value")
                                constrain(graphBar){
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                    start.linkTo(parent.start)
                                }
                                constrain(graphValue){
                                    bottom.linkTo(graphBar.top)
                                    end.linkTo(graphBar.end)
                                    start.linkTo(graphBar.start)
                                }
                            }

                            ConstraintLayout(constraints) {
                                Box(
                                    modifier = Modifier
                                        .layoutId("graph_bar")
                                        .clip(barShape)
                                        .fillMaxWidth()
                                        .fillMaxHeight(value)
                                        .background(barColor)
                                )
                                Text(
                                    text = "${value.toInt()}" ,
                                    modifier = Modifier.layoutId("graph_value")
                                )
                            }

                        }

                        // scale x-axis and bottom part of graph
                        Column(
                            modifier = Modifier
                                .height(xAxisScaleHeight),
                            verticalArrangement = Top,
                            horizontalAlignment = CenterHorizontally
                        ) {

                            // small vertical line joining the horizontal x-axis line
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 2.dp,
                                            bottomEnd = 2.dp
                                        )
                                    )
                                    .width(horizontalLineHeight)
                                    .height(lineHeightXAxis)
                                    .background(color = Color.Transparent)
                            )
                            // scale x-axis
                            Text(
                                modifier = Modifier.padding(bottom = 3.dp),
                                text = key.toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )

                        }

                    }
                }

                /*// Graph
                graphBarData.forEachIndexed { index, value ->

                  *//*  var animationTriggered by remember {
                        mutableStateOf(false)
                    }
                    val graphBarHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) value else 0f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 0
                        ), label = ""
                    )
                    LaunchedEffect(key1 = true) {
                        animationTriggered = true
                    }*//*

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Top,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        // Each Graph
                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .clip(barShape)
                                .width(barWidth)
                                .height(height - 10.dp)
                                .background(Color.Transparent),
                            contentAlignment = BottomCenter
                        ) {

                            val constraints = ConstraintSet {
                                val graphBar = createRefFor("graph_bar")
                                val graphValue = createRefFor("graph_value")
                                constrain(graphBar){
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                    start.linkTo(parent.start)
                                }
                                constrain(graphValue){
                                    bottom.linkTo(graphBar.top)
                                    end.linkTo(graphBar.end)
                                    start.linkTo(graphBar.start)
                                }
                            }

                            ConstraintLayout(constraints) {
                                Box(
                                    modifier = Modifier
                                        .layoutId("graph_bar")
                                        .clip(barShape)
                                        .fillMaxWidth()
                                        .fillMaxHeight(value)
                                        .background(barColor)
                                )
                                Text(
                                    text = "50" ,
                                    modifier = Modifier.layoutId("graph_value")
                                )
                            }

                        }

                        // scale x-axis and bottom part of graph
                        Column(
                            modifier = Modifier
                                .height(xAxisScaleHeight),
                            verticalArrangement = Top,
                            horizontalAlignment = CenterHorizontally
                        ) {

                            // small vertical line joining the horizontal x-axis line
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 2.dp,
                                            bottomEnd = 2.dp
                                        )
                                    )
                                    .width(horizontalLineHeight)
                                    .height(lineHeightXAxis)
                                    .background(color = Color.Transparent)
                            )
                            // scale x-axis
                            Text(
                                modifier = Modifier.padding(bottom = 3.dp),
                                text = xAxisScaleData[index].toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )

                        }

                    }

                }*/

            }

            // horizontal line on x-axis below the graph
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .padding(bottom = xAxisScaleHeight + 3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .fillMaxWidth()
                        .height(horizontalLineHeight)
                        .background(Color.Transparent)
                )

            }


        }


    }

}