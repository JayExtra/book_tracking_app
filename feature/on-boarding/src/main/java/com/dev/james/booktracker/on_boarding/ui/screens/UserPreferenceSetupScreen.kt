package com.dev.james.booktracker.on_boarding.ui.screens

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.james.booktracker.compose_ui.ui.theme.Brown
import com.dev.james.booktracker.compose_ui.ui.theme.BrownLight
import com.dev.james.booktracker.compose_ui.ui.theme.GrayHue
import com.dev.james.booktracker.compose_ui.ui.theme.Orange
import com.dev.james.booktracker.on_boarding.ui.components.*
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
                    Modifier
                        .fillMaxWidth()
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
                    1 ->  AvatarGridSection(){
                            Toast.makeText(context, "avatar $it selected", Toast.LENGTH_SHORT).show()
                        }
                    2 ->  GenreSelectionSection(){
                        Toast.makeText(context, "$it genre selected", Toast.LENGTH_SHORT).show()
                    }
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
                    delayMillis = 10

                    ,
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
                    backgroundColor = Color.Transparent,
                    outlineColor = Orange ,
                    textColor = Orange
                ) {
                    //execute click action
                    onPreviousClicked()
                }
            }



            StatefulRoundOutlineButton(
                text = if (currentPosition == 3) "Finish" else "Next",
                backgroundColor = Color.Transparent ,
                outlineColor = Orange ,
                textColor = Orange
            ) {
                //execute click action
                onNextClicked()
            }


        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AvatarGridSection(
    modifier: Modifier = Modifier ,
    avatarSelected : (Int) -> Unit
){
    var currentSelectedAvatar by remember {
        mutableStateOf(0)
    }
    val avatarImageList = listOf(
        R.drawable.dragon,
        R.drawable.iron_man,
        R.drawable.princess,
        R.drawable.warrior,
        R.drawable.knight,
        R.drawable.super_mario,
        R.drawable.king,
        R.drawable.lego_head,
        R.drawable.steampunk,
    )
   Column(
       verticalArrangement = Arrangement.Center ,
       horizontalAlignment = Alignment.CenterHorizontally,
       modifier = modifier.fillMaxWidth()
   ) {
       Spacer(modifier = modifier
           .fillMaxWidth()
           .height(60.dp))

       Text(
           text = "Please select your avatar" ,
           style = MaterialTheme.typography.body1,
           modifier = Modifier.padding(16.dp)
       )
       val cellConfiguration = if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE) {
           GridCells.Adaptive(minSize = 175.dp)
       } else GridCells.Fixed(3)

      LazyVerticalGrid(
          modifier  = Modifier.padding(32.dp),
          columns = cellConfiguration ,
          verticalArrangement = Arrangement.spacedBy(16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
      ){
          items(avatarImageList){
                AvatarItem(
                    image = it ,
                    isSelected = currentSelectedAvatar == it
                ){ avatarResource ->
                    avatarSelected(avatarResource)
                    currentSelectedAvatar = avatarResource
                }
          }
      }
   }
}

@Composable
fun AvatarItem(
    image : Int ,
    isSelected : Boolean = false ,
    avatarSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                color = if (isSelected) Orange else Color.Transparent,
                width = 2.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(Color.White)
            .clickable {
                avatarSelected(image)
            }
            ,
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = image) ,
            contentDescription = "Avatar images",
            contentScale = ContentScale.Crop ,
        )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenreSelectionSection(
    modifier: Modifier = Modifier ,
    genreSelected : (String) -> Unit
){
    val genreList = listOf(
        "Crime" ,
        "Fiction" ,
        "Action and Adventure" ,
        "Thriller" ,
        "Horror" ,
        "Drama" ,
        "Romance" ,
        "Sci-fi" ,
        "Erotic" ,
        "Legal" ,
        "Scientific" ,
        "Educational" ,
        "Motivational" ,
        "Fitness and Health" ,
        "Sports" ,
        "Film" ,
        "Autobiography"
    )
    val selectedGenres =  remember { mutableStateListOf<String>() }

    Column(
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
        )
        Text(
            text = "Please select your favourite genres" ,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp)
        )

        val cellConfiguration = if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE) {
            StaggeredGridCells.Adaptive(minSize = 175.dp)
        } else StaggeredGridCells.Fixed(4)

        LazyHorizontalStaggeredGrid(
            modifier  = Modifier.padding( bottom = 100.dp , start = 16.dp  , top = 70.dp) ,
            rows = cellConfiguration ,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(genreList){genre ->
                SelectableChip(chipIsSelected = {
                    if(selectedGenres.contains(it)){
                        selectedGenres.remove(it)
                    }else {
                        selectedGenres.add(it)
                    }
                    genreSelected(genre)

                }, text = genre ,
                    isChipSelected = selectedGenres.contains(genre)
                )
            }
        }
        
    }
}