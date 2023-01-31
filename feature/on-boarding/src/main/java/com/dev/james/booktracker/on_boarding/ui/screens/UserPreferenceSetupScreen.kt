package com.dev.james.booktracker.on_boarding.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.james.booktracker.compose_ui.ui.theme.Brown
import com.dev.james.booktracker.compose_ui.ui.theme.Orange
import com.dev.james.booktracker.on_boarding.ui.components.HorizontalStepsProgressBarPreview
import com.dev.james.booktracker.on_boarding.ui.components.RoundedInputText
import com.dev.james.booktracker.on_boarding.ui.components.StatefulRoundOutlineButton
import com.dev.james.booktracker.on_boarding.ui.components.StepsProgressBar
import com.dev.james.booktracker.on_boarding.ui.navigation.UserSetupScreenNavigator
import com.dev.james.booktracker.on_boarding.ui.viewmodel.UserPreferenceSetupViewModel
import com.dev.james.on_boarding.R
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun UserPreferenceSetupScreen(
    userSetupScreenNavigator : UserSetupScreenNavigator ,
    userPreferenceSetupViewModel: UserPreferenceSetupViewModel = hiltViewModel()
){

    var currentPosition by remember {
        mutableStateOf(0)
    }
    var previousPosition by remember {
        mutableStateOf( currentPosition - 1)
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.Top ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

            Column(
                modifier = Modifier.weight(0.1f) ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp))

                StepsProgressBar(
                    Modifier.fillMaxWidth()
                        .padding(start = 60.dp) ,
                    numberOfSteps = 4 ,
                    currentStep = currentPosition /* will be updated by state*/
                )
            }

            Column(
                modifier = Modifier.weight(0.5f) ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //depending on the position we are in , we will
                // swap with different section i.e name section
                // avatar selection section , material theme section etc
                when(currentPosition){
                    0 ->  NameSection(text = "Some text" , onValueChanged = { })
                    1 ->  AvatarGridSection()
                    2 ->  GenreSelectionSection()
                    3 ->  ThemeSection()
                }

            }

            BottomNextPreviousButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) ,
                onNextClicked = {
                    /* update the current position we are in during on boarding*/
                    if(currentPosition < 3){
                        currentPosition += 1
                    }else {
                        //navigate to home screen
                        Toast.makeText(
                            context ,
                            "Reached the end of the process" ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } ,
                onPreviousClicked = {
                    /* update the current position we are in during on boarding*/
                    if(currentPosition > 0){
                        currentPosition -= 1
                    }else {
                        Toast.makeText(
                            context ,
                            "Back at the beggining" ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } ,
                previousPosition = previousPosition ,
                currentPosition = currentPosition
            )


    }

}

@Composable
@Preview(device = Devices.PIXEL_4_XL , showBackground = true)
fun UserPreferenceSetupScreenPreview(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Column(
            modifier = Modifier.weight(0.1f) ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(60.dp))
            HorizontalStepsProgressBarPreview()
        }
        
        Column(
            modifier = Modifier.weight(0.5f) ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //depending on the position we are in , we will
            // swap with different section i.e name section
            // avatar selection section , material theme section etc
           NameSection(text = "Some text" , onValueChanged = { })
        }

        BottomNextPreviousButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) ,
            onNextClicked = {} ,
            onPreviousClicked = {} ,
            previousPosition = 0 ,
            currentPosition = 0
        )

    }

}

@Composable
fun NameSection(
    modifier: Modifier = Modifier ,
    text : String ,
    onValueChanged : (String) -> Unit
){
    Column(
        modifier = modifier ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier
            .fillMaxWidth()
            .height(90.dp))
        Text(
            text = "What is your name? " ,
            style = MaterialTheme.typography.body1
        )

        RoundedInputText(modifier = modifier
            .fillMaxWidth()
            .padding(32.dp) ,
            onValueChanged = {
                onValueChanged(it)
            } ,
            icon = R.drawable.outline_account_circle_24 ,
            text = text
        )
    }
}

@Composable
fun BottomNextPreviousButtons(
    modifier: Modifier = Modifier ,
    onNextClicked : () -> Unit ,
    onPreviousClicked : () -> Unit ,
    currentPosition : Int  ,
    previousPosition: Int  ,
){
    Box(
        modifier = modifier ,
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth() ,
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = if (currentPosition > 0) Arrangement.SpaceBetween else Arrangement.End
        ){

            AnimatedVisibility(
                visible = currentPosition > 0 ,
                enter = fadeIn(animationSpec = tween(
                    durationMillis = 100 ,
                    delayMillis = 10 ,
                    easing = FastOutSlowInEasing
                )) ,
                exit = fadeOut(animationSpec = tween(
                    durationMillis = 100 ,
                    delayMillis = 10 ,
                    easing = FastOutSlowInEasing
                ))
            ) {
                StatefulRoundOutlineButton(
                    text = "Previous",
                    backgroundColor = Color.White ,
                    outlineColor = Orange ,
                    textColor = Orange
                ) {
                    //execute click action
                    onPreviousClicked()
                }
            }



            StatefulRoundOutlineButton(
                text = if (currentPosition == 3) "Finish" else "Next",
                backgroundColor = Color.White ,
                outlineColor = Orange ,
                textColor = Orange
            ) {
                //execute click action
                onNextClicked()
            }


        }

    }

}

@Composable
fun AvatarGridSection(
    modifier: Modifier = Modifier
){
   Box(
       contentAlignment = Alignment.Center ,
       modifier = modifier.fillMaxWidth()
   ) {
      Text(text = "2", fontSize = 32.sp , modifier = Modifier.padding(32.dp))
   }
}

@Composable
fun ThemeSection(
    modifier: Modifier = Modifier
){
    Box(
        contentAlignment = Alignment.Center ,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "4", fontSize = 32.sp , modifier = Modifier.padding(32.dp))
    }
}

@Composable
fun GenreSelectionSection(
    modifier: Modifier = Modifier
){
    Box(
        contentAlignment = Alignment.Center ,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "3", fontSize = 32.sp , modifier = Modifier.padding(32.dp))
    }
}