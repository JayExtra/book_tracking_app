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
import com.dev.james.booktracker.compose_ui.ui.utils.formatTimeToDHMS
import timber.log.Timber

@Composable
@Preview(showBackground = true)
fun BarGraph(
    modifier: Modifier = Modifier,
    graphBarData : Map<String , Long> = mapOf("Sun" to 7200000L , "Mon" to 3600000L , "Teu" to 1800000L , "Wen" to 1200000L , "Thur" to 3600000L , "Fri" to 2400000L , "Sat" to 600000L
    ),
    xAxisScaleData: List<Int> = listOf(),
    targetDuration : Long = 7200000L,
    height: Dp = 380.dp,
    roundType: BarType = BarType.CIRCULAR_TYPE,
    barWidth: Dp = 38.dp,
    barColor: Color = MaterialTheme.colorScheme.primary,
    barArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly
) {

    /*var targetTime by remember {
        mutableStateOf(_targetTime + 0)
    }*/

    // for getting screen width and height you can use LocalConfiguration
    val configuration = LocalConfiguration.current
    // getting screen width
    val width = configuration.screenWidthDp.dp

    // bottom height of the X-Axis Scale
    //controls generally the height of the dotted x-axis
    val xAxisScaleHeight = 20.dp

    //this moves the entire dotted line x-axis value either up or down
    val yAxisScaleSpacing by remember {
        mutableStateOf(100f)
    }

    //controls the space between the bar lines especially the y-axis values
    val yAxisTextWidth by remember {
        mutableStateOf(10.dp)
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
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    // for y coordinates of y-axis scale to create horizontal dotted line indicating y-axis scale
    val yCoordinates = mutableListOf<Float>()
    // for dotted line effect
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    // height of vertical line over x-axis scale connecting x-axis horizontal line
    val lineHeightXAxis = 30.dp
    // height of horizontal line over x-axis scale
    val horizontalLineHeight = 20.dp

    //main box that stacks both the dotted y axis and the graph itself
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // y-axis scale and horizontal dotted lines on graph indicating y-axis scale
        //the dotted line y-axis values
        /*    Column(
                modifier = Modifier
                    .padding(top = xAxisScaleHeight, end = 3.dp)
                    .height(height)
                    .fillMaxWidth(),
                horizontalAlignment = CenterHorizontally
            ) {


                Canvas(modifier = Modifier.padding(bottom = 26.dp).fillMaxSize()) {
                    // Y-Axis Scale Text
                    (0..2).forEach { i ->
                        val officialTarget = (targetTime * i) / 2
                        val labelColor = if(_targetTime == officialTarget) Color.Green else Color.Black
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                "$officialTarget $hrsMin",
                                30f,
                                size.height - yAxisScaleSpacing - i * size.height / 3f,
                                textPaint.apply { color = labelColor.hashCode() }
                            )
                        }
                        yCoordinates.add(size.height - yAxisScaleSpacing - i * size.height / 3f)
                    }

                    // horizontal dotted lines on graph indicating y-axis scale
                    (1..2).forEach {
                        drawLine(
                            start = Offset(x = yAxisScaleSpacing +30f, y = yCoordinates[it]),
                            end = Offset(x= size.width, y = yCoordinates[it]),
                            color = if(it == 2) Color.Green else Color.Gray,
                            strokeWidth = 5f,
                            pathEffect = pathEffect
                        )
                    }

                }

            }*/
        // Graph with Bar Graph and X-Axis Scale
        // main graph

        //Get maximum value in graph for normalization later
        val durationTemp = graphBarData.maxByOrNull { it.value }?.value ?: 1L
        val maxDuration = if(durationTemp == 0L) 1L else durationTemp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.Center
        ) {

            //row to show each graph bar
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ) {
                graphBarData.entries.toList().forEach { entry ->

                    val (dayOfWeek , duration) = entry


                    val normalizedGraphHeight = duration.toFloat() / maxDuration.toFloat()

                    Timber.tag("BarGraph").d("Normalized graph height=> $normalizedGraphHeight , maxDuration => $maxDuration , duration => $duration")

                    val finalBarColor = if(duration >= targetDuration ) Color.Green else barColor

                    var animationTriggered by remember {
                        mutableStateOf(false)
                    }

                    val graphBarHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) normalizedGraphHeight else 0f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 0
                        ), label = ""
                    )
                    LaunchedEffect(key1 = true) {
                        animationTriggered = true
                    }

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
                                    top.linkTo(graphValue.bottom , 4.dp)
                                }
                                constrain(graphValue){
                                    bottom.linkTo(graphBar.top)
                                    end.linkTo(graphBar.end)
                                    start.linkTo(graphBar.start)
                                    top.linkTo(parent.top , 20.dp)
                                }
                            }

                            ConstraintLayout(
                               constraintSet =  constraints ,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .layoutId("graph_bar")
                                        .clip(barShape)
                                        .fillMaxWidth()
                                        .fillMaxHeight(graphBarHeight)
                                        .background(finalBarColor)
                                )
                                Text(
                                    text = duration.formatTimeToDHMS() ,
                                    modifier = Modifier.layoutId("graph_value"),
                                    fontSize = 12.sp
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

                            /*    // small vertical line joining the horizontal x-axis line
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
                                )*/
                            // scale x-axis
                            Text(
                                modifier = Modifier.padding(bottom = 3.dp),
                                text = dayOfWeek,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )

                        }

                    }

                }

              /*  for((key , value) in graphBarData){

                    var animationTriggered by remember {
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
                    }

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
                                        .fillMaxHeight(graphBarHeight)
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

                        *//*    // small vertical line joining the horizontal x-axis line
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
                            )*//*
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
                }*/

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