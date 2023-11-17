package com.dev.james.booktracker.home.presentation.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.*
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.bottom_sheets.PdfListBottomSheetContent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppShapes
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.R
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.GoalProgressData
import com.dev.james.booktracker.home.presentation.components.BookGoalInfoComponent
import com.dev.james.booktracker.home.presentation.components.MyGoalsCardComponent
import com.dev.james.booktracker.home.presentation.components.StreakComponent
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.dev.james.booktracker.home.presentation.viewmodels.HomeScreenViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import timber.log.Timber
import java.security.Permission

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
@Destination
fun HomeScreen(
    homeNavigator: HomeNavigator,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val homeScreenState = homeScreenViewModel.homeScreenUiState.collectAsStateWithLifecycle()
    var isGrid by rememberSaveable {
        mutableStateOf(true)
    }

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
        confirmValueChange = {
            it != SheetValue.Hidden
        }
    )

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val coroutineScope = rememberCoroutineScope()


    val storagePermissionState =
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val storagePermissionDialogRationaleState = rememberMaterialDialogState()

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission has been granted", Toast.LENGTH_SHORT).show()
                //launch camera picker screen here
                coroutineScope.launch {
                    sheetState.expand()
                }
            } else {
                //show rationale dialog
                storagePermissionDialogRationaleState.show()
            }
        }

    BottomSheetScaffold(
        modifier = Modifier.testTag("pdf_bottom_sheet"),
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetDragHandle = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, start = 8.dp, bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pdf books available", modifier = Modifier.weight(0.5f))

                    IconButton(
                        onClick = {
                            isGrid = !isGrid
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            painter = if (isGrid) painterResource(id = com.dev.james.booktracker.home.R.drawable.ic_grid_view_24) else painterResource(
                                id = com.dev.james.booktracker.home.R.drawable.ic_view_list_24
                            ),
                            contentDescription = "list or grid view",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "close bottom sheet ",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        sheetShape = RoundedCornerShape(0.dp),
        sheetTonalElevation = 5.dp,
        // sheetPeekHeight = 0.dp,
        sheetSwipeEnabled = false,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(900.dp),
                contentAlignment = Alignment.Center
            ) {
                //call our google bottom sheet here
                PdfListBottomSheetContent(
                    isGrid = isGrid,
                    pdfBookItems = listOf(
                        Book(
                            bookImage = "",
                            bookTitle = "Title A",
                            bookAuthors = listOf("Author A")
                        ),
                        Book(
                            bookImage = "",
                            bookTitle = "Title B",
                            bookAuthors = listOf("Author B")
                        ),
                        Book(
                            bookImage = "",
                            bookTitle = "Title C",
                            bookAuthors = listOf("Author C")
                        ),
                        Book(
                            bookImage = "",
                            bookTitle = "Title D",
                            bookAuthors = listOf("Author D")
                        ),
                        Book(
                            bookImage = "",
                            bookTitle = "Title E",
                            bookAuthors = listOf("Author E")
                        ),
                        Book(
                            bookImage = "",
                            bookTitle = "Title F",
                            bookAuthors = listOf("Author F")
                        ),
                    )
                )
            }
        }
    ) {
        StatelessHomeScreen(
            homeScreenState = homeScreenState.value,
            onAddButtonClick = {
                // Toast.makeText(context , "add button clicked", Toast.LENGTH_SHORT).show()
                homeNavigator.openReadGoalsScreen()
            },
            onContinueBtnClicked = { bookId ->
                homeNavigator.openTrackingScreen(bookId)
            },
            onProceedClicked = {

                //check if storage permission has been granted
                Timber.tag("HomeScreen")
                    .d("Storage permission states isGranted => ${storagePermissionState.status.isGranted}")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    coroutineScope.launch {
                        sheetState.expand()
                    }
                } else {
                    when {
                        storagePermissionState.status.isGranted -> {
                            coroutineScope.launch {
                                sheetState.expand()
                            }
                        }
                        storagePermissionState.status.shouldShowRationale -> {
                            storagePermissionDialogRationaleState.show()
                        }
                        else -> {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                }

            }
        )
    }

    MaterialDialog(
        dialogState = storagePermissionDialogRationaleState,
        backgroundColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp)
    ) {


        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {

            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = "warning image",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )

            val rationalText =
                if (!storagePermissionState.status.isGranted && !storagePermissionState.status.shouldShowRationale) {
                    //camera permission is fully denied by user
                    stringResource(com.dev.james.booktracker.home.R.string.storage_rationale_message_redirect)
                } else {
                    stringResource(com.dev.james.booktracker.home.R.string.storage_permission_rationale_message)
                }

            Text(
                text = rationalText,
                style = BookAppTypography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            RoundedBrownButton(
                label = "Grant permission.",
                onClick = {
                    if (!storagePermissionState.status.isGranted && !storagePermissionState.status.shouldShowRationale) {
                        //camera permission is fully denied by user
                        val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        context.startActivity(
                            intent
                        )

                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                    storagePermissionDialogRationaleState.hide()
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }


    }

}

@Composable
@Preview("Home Screen", showBackground = true)
fun StatelessHomeScreen(
    homeScreenState: HomeScreenViewModel.HomeScreenUiState = HomeScreenViewModel.HomeScreenUiState.HasFetchedScreenData(
        BookProgressData(),
        GoalProgressData()
    ),
    context: Context = LocalContext.current,
    onAddButtonClick: () -> Unit = {},
    onAddFabClick: () -> Unit = {},
    onContinueBtnClicked: (String) -> Unit = {},
    onProceedClicked: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter,

        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp)
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            when (homeScreenState) {
                is HomeScreenViewModel.HomeScreenUiState.HasFetchedScreenData -> {
                    if (homeScreenState.bookProgressData.bookId.isBlank() && homeScreenState.goalProgressData.goalId.isBlank()) {
                        EmptyAnimationSection(
                            animation = LottieCompositionSpec.RawRes(R.raw.shake_a_empty_box),
                            shouldShow = true,
                            message = "No goals currently set. Click the button below to set a reading goal."
                        )

                        ElevatedButton(
                            onClick = { onAddButtonClick() },
                            shape = BookAppShapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add button icon",
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "Add goals and current read",
                                style = BookAppTypography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                        }
                    } else {

                        /*Toast.makeText(
                            context ,
                            homeScreenState.bookGoalData.toString() ,
                            Toast.LENGTH_SHORT
                        ).show()*/

                        Timber.tag("HomeScreen").d("data => ${homeScreenState.bookProgressData}")

                        Spacer(modifier = Modifier.height(12.dp))

                        BookGoalInfoComponent(
                            shouldNotShowBlankMessage = homeScreenState.bookProgressData.bookId.isNotBlank(),
                            bookProgressData = homeScreenState.bookProgressData,
                            onContinueClicked = {
                                onContinueBtnClicked(
                                    homeScreenState.bookProgressData.bookId
                                )
                            },
                            onProceedToMyLibrary = {
                                onProceedClicked()
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (homeScreenState.goalProgressData.goalId.isNotBlank()) {

                            StreakComponent(
                                booksReadCount = homeScreenState.goalProgressData.booksRead,
                                targetBooks = homeScreenState.goalProgressData.booksToRead,
                                streakCount = 3
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            MyGoalsCardComponent()
                        }

                    }
                }

                else -> {}
            }

            Spacer(
                modifier = Modifier
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

