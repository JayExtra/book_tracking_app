package com.dev.james.booktracker.on_boarding.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.GrayHue
import com.dev.james.booktracker.compose_ui.ui.theme.Orange

@Composable
fun StepsProgressBar(modifier: Modifier = Modifier, numberOfSteps: Int, currentStep: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 0 until numberOfSteps) {
            Step(
                modifier = Modifier.weight(1F),
                isCompete = step < currentStep,
                isCurrent = step == currentStep ,
                isLast = step == numberOfSteps - 1
            )
        }
    }
}

@Composable
fun Step(modifier: Modifier = Modifier, isCompete: Boolean, isCurrent: Boolean , isLast : Boolean) {
    val color by animateColorAsState(
        targetValue = if (isCompete || isCurrent) Orange else GrayHue ,
        animationSpec = tween(
            delayMillis = 200 ,
            easing = FastOutLinearInEasing ,
            durationMillis = 2000
        )
    )
    val innerCircleColor by animateColorAsState(
        targetValue = if (isCompete) Orange else GrayHue ,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
    )


    Box(modifier = modifier) {

        //Line
        if(!isLast){
            Divider(
                modifier = Modifier.align(Alignment.CenterStart),
                color = color,
                thickness = 4.dp
            )
        }

        //Circle
        Canvas(modifier = Modifier
            .size(15.dp)
            .align(Alignment.CenterStart)
            .border(
                shape = CircleShape,
                width = 2.dp,
                color = color
            ),
            onDraw = {
                drawCircle(color = innerCircleColor)
            }
        )
    }
}

@Preview
@Composable
fun HorizontalStepsProgressBarPreview() {
    val currentStep = remember { mutableStateOf(1) }
    StepsProgressBar(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 60.dp),
        numberOfSteps = 4,
        currentStep = currentStep.value
    )
}