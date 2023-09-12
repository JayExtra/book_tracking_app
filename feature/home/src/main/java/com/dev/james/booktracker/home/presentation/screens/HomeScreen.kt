package com.dev.james.booktracker.home.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.*
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppShapes
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.R
import com.dev.james.booktracker.core.common_models.BookGoalData
import com.dev.james.booktracker.home.presentation.components.BookGoalInfoComponent
import com.dev.james.booktracker.home.presentation.components.MyGoalsCardComponent
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.dev.james.booktracker.home.presentation.viewmodels.HomeScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import timber.log.Timber

@Composable
@Destination
fun HomeScreen(
    homeNavigator: HomeNavigator ,
    homeScreenViewModel : HomeScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val homeScreenState = homeScreenViewModel.homeScreenUiState.collectAsStateWithLifecycle()
    StatelessHomeScreen(
        homeScreenState = homeScreenState.value,
        onAddButtonClick = {
           // Toast.makeText(context , "add button clicked", Toast.LENGTH_SHORT).show()
            homeNavigator.openReadGoalsScreen()
        }
    ){
       // Toast.makeText(context , "add button FAB clicked", Toast.LENGTH_SHORT).show()
       homeNavigator.openReadGoalsScreen()
    }
}

@Composable
@Preview("Home Screen" , showBackground = true)
fun StatelessHomeScreen(
    homeScreenState : HomeScreenViewModel.HomeScreenUiState = HomeScreenViewModel.HomeScreenUiState.HasFetchedScreenData(
        BookGoalData()
    ),
    context : Context = LocalContext.current,
    onAddButtonClick : () -> Unit = {},
    onAddFabClick : () -> Unit = {}
){
    Box(
        modifier = Modifier.fillMaxSize() ,
        contentAlignment = Alignment.TopCenter
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            when(homeScreenState){
                is HomeScreenViewModel.HomeScreenUiState.HasFetchedScreenData -> {
                    if(homeScreenState.bookGoalData.bookId.isBlank()) {
                        EmptyAnimationSection(
                            animation = LottieCompositionSpec.RawRes(R.raw.shake_a_empty_box) ,
                            shouldShow = true ,
                            message = "No goals or current read set. Click the button below to add a current read and goals."
                        )

                        ElevatedButton(
                            onClick = { onAddButtonClick() } ,
                            shape = BookAppShapes.medium ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ) ,
                            contentPadding = PaddingValues(start = 16.dp , end = 16.dp , top = 8.dp , bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add button icon" ,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "Add goals and current read" ,
                                style = BookAppTypography.labelMedium ,
                                color = MaterialTheme.colorScheme.primary
                            )

                        }
                    }else {

                        /*Toast.makeText(
                            context ,
                            homeScreenState.bookGoalData.toString() ,
                            Toast.LENGTH_SHORT
                        ).show()*/

                        Timber.tag("HomeScreen").d("data => ${homeScreenState.bookGoalData}")

                        BookGoalInfoComponent(
                            bookGoalData = homeScreenState.bookGoalData
                        )

                        MyGoalsCardComponent()
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
            )
        }

/*
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = BookAppShapes.medium ,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onClick = {
                *//*navigate or show goal addition bottom sheet*//*
                onAddFabClick()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add action" ,
                tint = MaterialTheme.colorScheme.primary
            )
        }*/
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EmptyAnimationSection(
    modifier: Modifier = Modifier ,
    shouldShow : Boolean = false ,
    animation : LottieCompositionSpec.RawRes ,
    message : String = ""
){

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

            Spacer(modifier = Modifier
                .height(8.dp)
                .fillMaxWidth())


            Text(
                text = message,
                style = BookAppTypography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center ,
                modifier = Modifier.padding(start = 16.dp , end = 16.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )
        }

    }

}

