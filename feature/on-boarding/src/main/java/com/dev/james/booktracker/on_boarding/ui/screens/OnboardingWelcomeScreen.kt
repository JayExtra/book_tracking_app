@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.dev.james.booktracker.on_boarding.ui.screens

import android.widget.Toast
import androidx.annotation.RawRes
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.Brown
import com.dev.james.booktracker.on_boarding.ui.components.StatefulRoundOutlineButton
import com.dev.james.booktracker.on_boarding.ui.components.StatelessRoundOutlineButton
import com.dev.james.booktracker.on_boarding.ui.navigation.OnBoardingNavigator
import com.dev.james.booktracker.on_boarding.ui.viewmodel.OnBoardingWelcomeScreenViewModel
import com.dev.james.on_boarding.R
import com.ramcosta.composedestinations.annotation.Destination

/*
* Information screens on immediate launch of the application if user not on-boarded*/

@Composable
@Destination
//@Preview(name = "On-Boarding welcome screen", widthDp = 320, heightDp = 700)
fun OnBoardingWelcomeScreen(
    modifier: Modifier = Modifier ,
    onBoardingWelcomeScreenViewModel: OnBoardingWelcomeScreenViewModel = hiltViewModel() ,
    onBoardingNavigator : OnBoardingNavigator
) {

    val context = LocalContext.current
    var position by rememberSaveable {
        mutableStateOf(1)
    }
    Box(
        modifier = Modifier.fillMaxSize() ,
        contentAlignment = Alignment.Center
    ){

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Brown)
        ) {
            //pass in the button composable , progress , title texts and message texts

            OnBoardingMessageBlock(position = position)
            Spacer(modifier = Modifier.height(120.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.End ,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                StatefulRoundOutlineButton(
                    text = if(position == 3) "Finish" else "Next"
                ){
                    //Toast.makeText(context, "Next button clicked", Toast.LENGTH_SHORT).show()
                    /*onBoardingWelcomeScreenViewModel.finishOnBoardingStatus()
                    onBoardingNavigator.openHome()*/
                    if(position < 3){
                        position += 1
                    }else {
                        onBoardingWelcomeScreenViewModel.finishOnBoardingStatus()
                        onBoardingNavigator.openHome()
                    }

                }
            }
        }

        Box(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        bottomEnd = 50.dp,
                        bottomStart = 50.dp
                    )
                )
                .background(color = Color.White)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .fillMaxHeight(0.55f)
            ,
            contentAlignment = Alignment.Center
        ) {
            //pass in the lottie animation composable
           LottieAnimationSection( position = position)
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LottieAnimationSection(
    modifier: Modifier = Modifier,
    position: Int
){
    val anim = when(position){
        1 -> LottieCompositionSpec.RawRes(R.raw.books_reader_lottie)
        2 -> LottieCompositionSpec.RawRes(R.raw.notification_lottie)
        3 -> LottieCompositionSpec.RawRes(R.raw.monitor_lottie)
        else -> { LottieCompositionSpec.RawRes(R.raw.books_reader_lottie)  }
    }


    Box(
        contentAlignment = Alignment.Center ,
        modifier = Modifier.padding(16.dp)
    ){
        AnimatedContent(
            modifier = Modifier.fillMaxWidth() ,
            targetState = anim ,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it }
                ) + fadeIn() with slideOutHorizontally(
                    targetOffsetX = { -it }
                ) + fadeOut()
            }
        ) {
            val isLottiePlaying by remember{
                mutableStateOf(true)
            }
            val animationSpeed by remember {
                mutableStateOf(1f)
            }
            val composition by rememberLottieComposition(
                spec = anim
            )

            val lottieAnimationState by animateLottieCompositionAsState(
                composition = composition ,
                iterations = LottieConstants.IterateForever ,
                isPlaying = isLottiePlaying ,
                speed = animationSpeed ,
                restartOnPlay = true
            )

            LottieAnimation(
                composition = composition ,
                lottieAnimationState,
                modifier = Modifier.size(300.dp)
            )
        }
    }
    
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnBoardingMessageBlock(
    modifier: Modifier = Modifier ,
    position : Int
){
    /*val title = when(position){
        1 -> "Some title 1"
        2 -> "Some title 2"
        3 -> "Some title 3"
        else -> " "
    }
    val body = when(position){
        1 -> R.string.on_boarding_message_1
        2 -> R.string.on_boarding_message_2
        3 -> R.string.on_boarding_message_2
        else -> { 0 }
    }*/
    val (title , body) = when(position){
        1 -> Pair(R.string.on_boarding_title_1 , R.string.on_boarding_message_1)
        2 -> Pair(R.string.on_boarding_title_2 , R.string.on_boarding_message_2)
        3 -> Pair(R.string.on_boarding_title_3 , R.string.on_boarding_message_3)
        else -> Pair(R.string.on_boarding_title_1 , R.string.on_boarding_message_1)
    }

    Column(
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        AnimatedContent(
            modifier = Modifier.fillMaxWidth() ,
            targetState = title ,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it }
                ) + fadeIn() with slideOutHorizontally(
                    targetOffsetX = { -it }
                ) + fadeOut()
            }
        ) { targetState ->
            Text(
                text = stringResource(id = targetState) ,
                style = BookAppTypography.h5 ,
                textAlign = TextAlign.Center ,
                color = Color.White
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            modifier = Modifier.fillMaxWidth() ,
            targetState = body ,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { it }
                ) + fadeIn() with slideOutHorizontally(
                    targetOffsetX = { -it }
                ) + fadeOut()
            }
        ){ targetState ->
            Text(
                text = stringResource(id = targetState) ,
                style = BookAppTypography.body1 ,
                textAlign = TextAlign.Center ,
                color = Color.White
            )
        }

    }
}





