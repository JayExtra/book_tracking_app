package com.dev.james.booktracker.on_boarding.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.on_boarding.ui.components.RoundedBrownButton
import com.dev.james.booktracker.on_boarding.ui.navigation.HelloMessageNavigator
import com.dev.james.on_boarding.R
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun WelcomeHelloMessageScreen(
    modifier : Modifier = Modifier ,
    helloMessageNavigator: HelloMessageNavigator
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center ,
        modifier = Modifier.fillMaxSize()
    ) {
        WelcomeLottieAnimation(
            animation = LottieCompositionSpec.RawRes(R.raw.hello_lottie)
        )

            Text(
                modifier = Modifier.padding(start = 16.dp , end = 16.dp , bottom = 16.dp),
                text = stringResource(id = R.string.welcome_hello_message) ,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center ,
                color = MaterialTheme.colors.onPrimary
            )


        Spacer(modifier = Modifier.height(20.dp))

        RoundedBrownButton(text = "Lets get started!") {
            //trigger navigation
            helloMessageNavigator.navigateToUserDetailsCaptureScreen()
        }

        Spacer(modifier = Modifier.height(52.dp))

    }
}

@Composable
fun WelcomeLottieAnimation(
    modifier: Modifier = Modifier ,
    animation : LottieCompositionSpec.RawRes
){
    Box(contentAlignment = Alignment.TopCenter ) {
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
            iterations = 1,
            isPlaying = isLottiePlaying,
            speed = animationSpeed,
            restartOnPlay = true
        )

        LottieAnimation(
            composition = composition,
            lottieAnimationState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}


