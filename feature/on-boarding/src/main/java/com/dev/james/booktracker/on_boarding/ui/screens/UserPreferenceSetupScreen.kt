package com.dev.james.booktracker.on_boarding.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.Brown
import com.dev.james.booktracker.on_boarding.ui.components.HorizontalStepsProgressBarPreview
import com.dev.james.booktracker.on_boarding.ui.components.RoundedInputText
import com.dev.james.booktracker.on_boarding.ui.components.StatefulRoundOutlineButton
import com.dev.james.booktracker.on_boarding.ui.components.StepsProgressBar
import com.dev.james.on_boarding.R

@Composable
fun UserPreferenceSetupScreen(){

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
                    numberOfSteps = 4 ,
                    currentStep = 1 /* will be updated by state*/
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
                NameSection(text = "Some text" , onValueChanged = { })

            }

            BottomNextPreviousButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) ,
                onNextClicked = {
                    /* update the current position we are in during on boarding*/
                } ,
                onPreviousClicked = {
                    /* update the current position we are in during on boarding*/
                }
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
            onPreviousClicked = {}
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
    onPreviousClicked : () -> Unit
){
    Box(
        modifier = modifier ,
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth() ,
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            StatefulRoundOutlineButton(
                text = "Previous",
                backgroundColor = Color.White ,
                outlineColor = Brown ,
                textColor = Brown
            ) {
                //execute click action
                onPreviousClicked()
            }

            StatefulRoundOutlineButton(
                text = "Next",
                backgroundColor = Color.White ,
                outlineColor = Brown ,
                textColor = Brown
            ) {
                //execute click action
                onNextClicked()
            }


        }

    }

}