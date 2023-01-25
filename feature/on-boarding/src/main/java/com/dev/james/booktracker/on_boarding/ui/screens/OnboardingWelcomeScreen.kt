package com.dev.james.booktracker.on_boarding.ui.screens

import android.widget.Toast
import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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

            OnBoardingMessageBlock()
            Spacer(modifier = Modifier.height(80.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.End ,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                StatefulRoundOutlineButton(
                    text = "Next"
                ){
                    //Toast.makeText(context, "Next button clicked", Toast.LENGTH_SHORT).show()
                    onBoardingWelcomeScreenViewModel.finishOnBoardingStatus()
                    onBoardingNavigator.openHome()
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
                .fillMaxHeight(0.6f)
            ,
            contentAlignment = Alignment.Center
        ) {
            //pass in the lottie animation composable
           LottieAnimationSection(anim = LottieCompositionSpec.RawRes(R.raw.books_reader_lottie))
        }
    }

}

@Composable
fun LottieAnimationSection(
    modifier: Modifier = Modifier,
    @RawRes anim: LottieCompositionSpec.RawRes
){
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

    Box(
        contentAlignment = Alignment.Center ,
        modifier = Modifier.padding(16.dp)
    ){
        LottieAnimation(
            composition = composition ,
            lottieAnimationState,
            modifier = Modifier.size(300.dp)
        )
    }
    
}

@Composable
fun OnBoardingMessageBlock(
    modifier: Modifier = Modifier ,
    title : String = "Some title" ,
    body : String = "Some body message that will be long. I will try to center this text and show what it is all about. Hope it looks awesome!"
){
    Column(
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = title ,
            style = BookAppTypography.h5 ,
            textAlign = TextAlign.Center ,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = body ,
            style = BookAppTypography.body2 ,
            textAlign = TextAlign.Center ,
            color = Color.White
        )

    }
}





