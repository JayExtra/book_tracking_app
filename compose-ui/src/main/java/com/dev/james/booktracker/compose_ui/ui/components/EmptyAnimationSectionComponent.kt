package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EmptyAnimationSection(
    modifier: Modifier = Modifier,
    shouldShow: Boolean = false,
    animation: LottieCompositionSpec.RawRes,
    message: String = ""
) {

    AnimatedVisibility(
        visible = shouldShow,
        enter = scaleIn(
            animationSpec = spring(
                stiffness = Spring.StiffnessMediumLow
            )
        ),
        exit = scaleOut(
            animationSpec = spring(
                stiffness = Spring.StiffnessMediumLow
            )
        )

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val isLottiePlaying by remember {
                mutableStateOf(true)
            }
            val animationSpeed by remember {
                mutableStateOf(1f)
            }
            val composition by rememberLottieComposition(
                spec = animation
            )

            val lottieAnimationState by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                isPlaying = isLottiePlaying,
                speed = animationSpeed,
                restartOnPlay = true
            )

            LottieAnimation(
                composition = composition,
                lottieAnimationState,
                modifier = Modifier.size(200.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
            )


            Text(
                text = message,
                style = BookAppTypography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )
        }

    }

}