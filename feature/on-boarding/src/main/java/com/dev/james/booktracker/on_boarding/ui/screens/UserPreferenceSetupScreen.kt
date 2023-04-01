package com.dev.james.booktracker.on_boarding.ui.screens

import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.theme.GrayHue
import com.dev.james.booktracker.compose_ui.ui.theme.Orange40
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.on_boarding.ui.components.*
import com.dev.james.booktracker.on_boarding.ui.navigation.UserSetupScreenNavigator
import com.dev.james.booktracker.on_boarding.ui.states.ThemeItem
import com.dev.james.booktracker.on_boarding.ui.viewmodel.UserPreferenceSetupViewModel
import com.dev.james.on_boarding.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun UserPreferenceSetupScreen(
    userSetupScreenNavigator: UserSetupScreenNavigator,
    userPreferenceSetupViewModel: UserPreferenceSetupViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val screenState = userPreferenceSetupViewModel.prefScreenState.collectAsStateWithLifecycle()
    val currentPosition = screenState.value.currentPosition
    val previousPosition = screenState.value.previousPosition

    LaunchedEffect(key1 = true){
        userPreferenceSetupViewModel.prefScreenUiEvents
            .collectLatest { event ->
                when(event){
                    is UserPreferenceSetupViewModel.UserPreferenceSetupUiEvents.NavigateToHomeScreen -> {
                        userSetupScreenNavigator.navigateToHomeScreen()
                    }
                }
            }
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.weight(0.1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )

            StepsProgressBar(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 60.dp),
                numberOfSteps = 4,
                currentStep = screenState.value.currentPosition /* will be updated by state*/
            )
        }

        Box(
            modifier = Modifier.weight(0.8f),
            contentAlignment = Alignment.TopCenter
        ) {
            //depending on the position we are in , we will
            // swap with different section i.e name section
            // avatar selection section , material theme section etc


            androidx.compose.animation.AnimatedVisibility(
                visible = currentPosition == 0,
                enter = fadeIn() + slideInHorizontally { if (currentPosition > previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (currentPosition > previousPosition) -it else it }
            ) {
                NameSection(
                    text = screenState.value.userName, onValueChanged = { name ->
                        //call text update from viewmodel
                        userPreferenceSetupViewModel.setUserPreference(
                            UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
                                .UpdateUserNameUi(name = name)
                        )
                    },
                    errorMessage = screenState.value.userNameFieldError
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = currentPosition == 1,
                enter = fadeIn() + slideInHorizontally { if (currentPosition > previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (currentPosition > previousPosition) -it else it }
            ) {
                AvatarGridSection(
                    currentSelectedAvatar = screenState.value.currentSelectedAvatar ,
                    errorMessage = screenState.value.avatarSelectionError
                ) { avatarId ->
                    /*Toast.makeText(context, "avatar $avatarId selected", Toast.LENGTH_SHORT).show()*/
                    userPreferenceSetupViewModel.setUserPreference(
                        UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
                            .SelectAvatar(avatar = avatarId)
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = currentPosition == 2,
                enter = fadeIn() + slideInHorizontally { if (currentPosition > previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (currentPosition > previousPosition) -it else it }
            ) {
                GenreSelectionSection(
                    selectedGenres = screenState.value.genreList,
                    chipSelectionError = screenState.value.chipSelectionError
                ) { genre ->
                    //Toast.makeText(context, "$it genre selected", Toast.LENGTH_SHORT).show()
                    userPreferenceSetupViewModel.setUserPreference(
                        UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
                            .AddSelectedGenre(genre = genre)
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = currentPosition == 3,
                enter = fadeIn() + slideInHorizontally { if (currentPosition > previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (currentPosition > previousPosition) -it else it }
            ) {
                ThemeSection(
                    selectedTheme = screenState.value.currentSelectedTheme
                ) { themeId ->
                    /* Toast.makeText(context, "$it theme selected", Toast.LENGTH_SHORT).show()*/
                    userPreferenceSetupViewModel.setUserPreference(
                        UserPreferenceSetupViewModel.UserPreferenceSetupUiActions
                            .SelectTheme(theme = themeId)
                    )
                }
            }
        }

        BottomNextPreviousButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onNextClicked = {
                /* update the current position we are in during on boarding*/
               /* if (currentPosition < 3) {
                    previousPosition = currentPosition
                    currentPosition += 1
                } else {
                    //save user data and navigate to home screen
                    userPreferenceSetupViewModel.setUserPreference(
                        UserPreferenceSetupViewModel
                            .UserPreferenceSetupActions.SavePreferenceData
                    )
                }*/
                userPreferenceSetupViewModel.setUserPreference(
                    UserPreferenceSetupViewModel
                        .UserPreferenceSetupUiActions.MoveNext(
                            screenState.value.currentPosition
                        )
                )

                if(currentPosition == 3){
                    userPreferenceSetupViewModel.setUserPreference(
                        UserPreferenceSetupViewModel
                            .UserPreferenceSetupUiActions.SavePreferenceDataUi
                    )
                }

            },
            onPreviousClicked = {
                /* update the current position we are in during on boarding*/
              /*  if (currentPosition > 0) {
                    previousPosition = currentPosition
                    currentPosition -= 1
                } else {
                    Toast.makeText(
                        context,
                        "Back at the beggining",
                        Toast.LENGTH_SHORT
                    ).show()
                }*/
                userPreferenceSetupViewModel.setUserPreference(
                   UserPreferenceSetupViewModel
                       .UserPreferenceSetupUiActions.MovePrevious(
                           screenState.value.currentPosition
                       )
                )
            },
            previousPosition = previousPosition,
            currentPosition = currentPosition
        )


    }

}

@Composable
@Preview(device = Devices.PIXEL_4_XL, showBackground = true)
fun UserPreferenceSetupScreenPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.weight(0.1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
            HorizontalStepsProgressBarPreview()
        }


        Box(
            modifier = Modifier.weight(0.8f),
            contentAlignment = Alignment.TopCenter
        ) {
            //depending on the position we are in , we will
            // swap with different section i.e name section
            // avatar selection section , material theme section etc
            /* NameSection(text = "Some text", onValueChanged = { })*/
            GenreSelectionSection(
                selectedGenres = listOf(),
                genreSelected = {

                },
                chipSelectionError = "Some error message"
            )
        }

        BottomNextPreviousButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onNextClicked = {},
            onPreviousClicked = {},
            previousPosition = 0,
            currentPosition = 0
        )

    }

}

@Composable
fun NameSection(
    modifier: Modifier = Modifier,
    text: String,
    onValueChanged: (String) -> Unit,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(90.dp)
                .testTag("Spacer 1")
        )
        Text(
           modifier = Modifier.testTag("what is your name text"),
            text = "What is your name? ",
            style = MaterialTheme.typography.bodyLarge
        )

        RoundedInputText(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 32.dp)
               .testTag("rounded input text")
                      ,
            onValueChanged = {
                onValueChanged(it)
            },
            icon = R.drawable.outline_account_circle_24,
            text = text,
            isErrorEnabled = errorMessage != null
        )

        if (errorMessage != null) {
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .testTag("spacer 2")
            )
            Text(
                modifier = Modifier.testTag("error message"),
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }

    }
}

@Composable
fun BottomNextPreviousButtons(
    modifier: Modifier = Modifier,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    currentPosition: Int,
    previousPosition: Int,
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
                StatefulRoundOutlineButton(
                    text = "Previous",
                    backgroundColor = Color.Transparent,
                    outlineColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.onPrimary ,
                ) {
                    //execute click action
                    onPreviousClicked()
                }
            }



            StatefulRoundOutlineButton(
                text = if (currentPosition == 3) "Finish" else "Next",
                backgroundColor = Color.Transparent,
                outlineColor = MaterialTheme.colorScheme.onPrimary,
                textColor = MaterialTheme.colorScheme.onPrimary
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
    modifier: Modifier = Modifier,
    currentSelectedAvatar: Int = 0,
    errorMessage : String?,
    avatarSelected: (Int) -> Unit,
) {
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
        )

        Text(
            text = "Please select your avatar",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        if(errorMessage != null){
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(4.dp) ,
                color = MaterialTheme.colorScheme.error
            )
        }
        val cellConfiguration =
            if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE) {
                GridCells.Adaptive(minSize = 175.dp)
            } else GridCells.Fixed(3)

        LazyVerticalGrid(
            modifier = Modifier.padding(32.dp),
            columns = cellConfiguration,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(avatarImageList) {
                AvatarItem(
                    image = it,
                    isSelected = currentSelectedAvatar == it
                ) { avatarResource ->
                    avatarSelected(avatarResource)
                }
            }
        }
    }
}

@Composable
fun AvatarItem(
    image: Int,
    isSelected: Boolean = false,
    avatarSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Transparent,
                width = 4.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(MaterialTheme.colorScheme.onBackground)
            .clickable {
                avatarSelected(image)
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Avatar images",
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun ThemeSection(
    modifier: Modifier = Modifier,
    selectedTheme: Int = 0,
    context : Context = LocalContext.current ,
    onThemeSelected: (Int) -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        )

        Text(
            text = "Please select your favourable theme",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        val themeList = listOf<ThemeItem>(
            ThemeItem(
                id = Theme.FOLLOW_SYSTEM.themeValue,
                themeIcon = R.drawable.baseline_settings_24,
                themeName = "System Default",
                themeMessage = "Sticking with the status quo!"
            ),
            ThemeItem(
                id = Theme.MATERIAL_YOU.themeValue,
                themeIcon = R.drawable.outline_material_you,
                themeName = "Material you",
                themeMessage = "Express yourself freely!"
            ),
            ThemeItem(
                id = Theme.LIGHT_THEME.themeValue,
                themeIcon = R.drawable.outline_light_mode_24,
                themeName = "Light Mode",
                themeMessage = "A little sun don't burn!"
            ),
            ThemeItem(
                id = Theme.DARK_THEME.themeValue,
                themeIcon = R.drawable.baseline_dark_mode_24,
                themeName = "Dark Mode",
                themeMessage = "In with the dark side!"
            )
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            contentPadding = PaddingValues(bottom = 16.dp)

        ) {
            items(themeList) { themeItem ->
                ThemeCardItem(
                    themeName = themeItem.themeName,
                    themeMessage = themeItem.themeMessage,
                    themeIcon = themeItem.themeIcon,
                    isSelected = themeItem.id == selectedTheme
                ) {
                    onThemeSelected(themeItem.id)
                }
            }
        }

    }
}

@Composable
fun ThemeCardItem(
    modifier: Modifier = Modifier,
    themeName: String,
    themeMessage: String,
    themeIcon: Int,
    isSelected: Boolean = false,
    onThemeCardClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(8.dp)
            .clickable { onThemeCardClick() }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )

        ) {
            val (icon, title, body) = createRefs()

            Icon(
                painter = painterResource(id = themeIcon),
                contentDescription = "Theme type icon",
                modifier = Modifier.constrainAs(
                    icon
                ) {
                    top.linkTo(anchor = parent.top, margin = 16.dp)
                    start.linkTo(anchor = parent.start, margin = 16.dp)
                },
                tint = if (isSelected) Color.White  else MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = themeName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.constrainAs(
                    title
                ) {
                    top.linkTo(icon.top)
                    start.linkTo(icon.end, margin = 16.dp)
                    bottom.linkTo(icon.bottom, margin = 4.dp)
                    width = Dimension.fillToConstraints
                },
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = themeMessage,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.constrainAs(
                    body
                ) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(title.start)
                    width = Dimension.fillToConstraints
                },
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onPrimary
            )

        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenreSelectionSection(
    modifier: Modifier = Modifier,
    selectedGenres: List<String>,
    chipSelectionError: String?,
    genreSelected: (String) -> Unit
) {
    val genreList = listOf(
        "Crime",
        "Fiction",
        "Action and Adventure",
        "Thriller",
        "Horror",
        "Drama",
        "Romance",
        "Sci-fi",
        "Erotic",
        "Legal",
        "Scientific",
        "Educational",
        "Motivational",
        "Fitness and Health",
        "Sports",
        "Film",
        "Autobiography"
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        )
        Text(
            text = "Please select your favourite genres",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
        if (chipSelectionError != null) {
            Text(
                text = chipSelectionError,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp , bottom = 4.dp , start = 16.dp , end = 16.dp),
                color = MaterialTheme.colorScheme.error ,
                textAlign = TextAlign.Center
            )
        }

        val cellConfiguration =
            if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE) {
                StaggeredGridCells.Adaptive(minSize = 100.dp)
            } else StaggeredGridCells.Fixed(4)
        val paddingValues =
            if (LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE) {
                PaddingValues(start = 8.dp, end = 8.dp, bottom = 60.dp, top = 16.dp)
            } else {
                PaddingValues(start = 16.dp, bottom = 250.dp, top = 24.dp)
            }

        LazyHorizontalStaggeredGrid(
            rows = cellConfiguration,
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = ScrollableDefaults.flingBehavior()
        ) {
            items(genreList) { genre ->
                SelectableChip(
                    chipIsSelected = {
                        genreSelected(genre)
                    }, text = genre,
                    isChipSelected = selectedGenres.contains(genre)
                )
            }
        }

    }
}




