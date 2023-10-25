package com.dev.james.booktracker.home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton

@Composable
@Preview("BottomButtonsSection", showBackground = true)
fun BottomNextPreviousButtons(
    modifier: Modifier = Modifier,
    onNextClicked: () -> Unit = {},
    onPreviousClicked: () -> Unit = {},
    currentPosition: Int = 0 ,
    disableNext : Boolean = false
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (currentPosition > 0) Arrangement.SpaceBetween else Arrangement.End
        ) {

            AnimatedVisibility(
                visible = currentPosition > 0,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 100,
                        delayMillis = 10,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 100,
                        delayMillis = 10,
                        easing = FastOutSlowInEasing
                    )
                )
            ) {
                RoundedBrownButton(
                    label = "Previous",
                    cornerRadius = 8.dp
                ) {
                    onPreviousClicked()
                }
            }

            RoundedBrownButton(
                label = if (currentPosition == 1) "Finish" else "Next",
                cornerRadius = 8.dp ,
                color = if (disableNext) Color.LightGray else MaterialTheme.colorScheme.secondary,
                textColor = if(disableNext) Color.White else MaterialTheme.colorScheme.onPrimary
            ) {
                if (!disableNext){
                    onNextClicked()
                }
            }

        }


    }

}