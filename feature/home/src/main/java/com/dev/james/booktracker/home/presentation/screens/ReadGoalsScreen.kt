package com.dev.james.booktracker.home.presentation.screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.components.BottomNextPreviousButtons
import com.dev.james.booktracker.home.presentation.forms.CurrentReadForm
import com.dev.james.booktracker.home.presentation.forms.OverallGoalsForm
import com.dev.james.booktracker.home.presentation.forms.SpecificGoalsForm
import com.dev.james.booktracker.home.presentation.viewmodels.ImageSelectorUiState
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenState
import com.dsc.form_builder.BaseState
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.SelectState
import com.dsc.form_builder.TextFieldState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Destination
fun ReadGoalScreen(
    homeNavigator: HomeNavigator,
    readGoalsScreenViewModel: ReadGoalsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val currentReadFormState by remember { mutableStateOf(readGoalsScreenViewModel.currentReadFormState) }

    val overallGoalsFormState by remember { mutableStateOf(readGoalsScreenViewModel.overallGoalFormState) }

    val specificGoalsFormState by remember { mutableStateOf(readGoalsScreenViewModel.specificGoalsFormState) }

    val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState
        .collectAsStateWithLifecycle(
            initialValue = ImageSelectorUiState(),
            minActiveState = Lifecycle.State.STARTED
        )

    val readGoalsScreenUiState = readGoalsScreenViewModel.readGoalsScreenUiState
        .collectAsStateWithLifecycle(
            initialValue = ReadGoalsScreenState(),
            minActiveState = Lifecycle.State.STARTED
        )

    var checkedState by remember { mutableStateOf(false) }

    var isCameraButtonClicked by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val cameraPermissionRationalDialogState = rememberMaterialDialogState()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission has been granted", Toast.LENGTH_SHORT).show()
                //launch camera picker screen here
            } else {
                cameraPermissionRationalDialogState.show()
            }
        }

    LaunchedEffect(key1 = !isCameraButtonClicked) {
        //check if user has granted permission
        if (isCameraButtonClicked) {
            launcher.launch(Manifest.permission.CAMERA)
        } else {
            isCameraButtonClicked = false
        }
    }

    /*if (cameraPermissionState.status.isGranted) {
        Timber.d("Permission is granted")
        //update camera state
    }*/


    StatelessReadGoalScreen(
        currentReadFormState = currentReadFormState,
        uiState = readGoalsScreenUiState.value,
        overallGoalsFormState = overallGoalsFormState,
        specificGoalsFormState = specificGoalsFormState,
        alertSwitchState = checkedState,
        popBackStack = {
            homeNavigator.openHomeScreen()
        },
        onGoogleIconClicked = {

        },
        imageSelectorState = imageSelectorState.value,
        onSaveClicked = {

            val formValidationResult = currentReadFormState.validate()

            readGoalsScreenViewModel.validateImageSelected(
                imageSelectedUri = imageSelectorState.value.imageSelectedUri
            )

            if (formValidationResult && !imageSelectorState.value.isError) {

                val allChaptersState = currentReadFormState.getState<TextFieldState>("chapters")
                val currentChaptersState =
                    currentReadFormState.getState<TextFieldState>("current chapter")

                if (currentChaptersState.value.toInt() > allChaptersState.value.toInt()) {
                    Toast.makeText(
                        context,
                        "The current chapter you are in exceeds the total chapters in this book",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    //start saving process to db
                    Toast.makeText(
                        context,
                        "Form is properly filled , saving data...",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        },
        onImageSelectorClicked = {


            //update selector state to show circular progress
            readGoalsScreenViewModel.passAddReadFormAction(
                ReadGoalsScreenViewModel.AddReadFormUiActions.LaunchImagePicker
            )

            when {
                cameraPermissionState.status.isGranted -> {
                    //if so launch image picker
                    Toast.makeText(
                        context,
                        "Camera permission has been granted",
                        Toast.LENGTH_SHORT
                    ).show()
                    coroutineScope.launch {
                        delay(5000L)

                        readGoalsScreenViewModel.passAddReadFormAction(
                            ReadGoalsScreenViewModel.AddReadFormUiActions
                                .ImageSelected(imageUri = "some image uri")
                        )
                    }
                }

                cameraPermissionState.status == PermissionStatus.Denied(shouldShowRationale = false) -> {
                    isCameraButtonClicked = true
                }

                cameraPermissionState.status.shouldShowRationale -> {
                    cameraPermissionRationalDialogState.show()
                }

            }


        },
        onAlertSwitchChecked = { status ->
            checkedState = status
        },

        onNextClicked = {

            val supportedDays = listOf(
                "Select specific days", "Every day except"
            )

            when (readGoalsScreenUiState.value.currentPosition) {
                0 -> {

                    val allChaptersState = currentReadFormState.getState<TextFieldState>("chapters")
                    val currentChaptersState =
                        currentReadFormState.getState<TextFieldState>("current chapter")

                    readGoalsScreenViewModel.validateImageSelected(
                        imageSelectedUri = imageSelectorState.value.imageSelectedUri
                    )

                    if (currentReadFormState.validate() && !imageSelectorState.value.isError) {
                        if (currentChaptersState.value.toInt() > allChaptersState.value.toInt()) {
                            Toast.makeText(
                                context,
                                "The current chapter you are in exceeds the total chapters in this book",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            readGoalsScreenViewModel.passMainScreenActions(
                                action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                    currentPosition = readGoalsScreenUiState.value.currentPosition
                                )
                            )
                        }

                    }
                }

                1 -> {
                    val timeFieldState = overallGoalsFormState.getState<TextFieldState>("time")
                    val freqFieldState =
                        overallGoalsFormState.getState<ChoiceState>("frequency field")
                    val daysSelectedState =
                        overallGoalsFormState.getState<SelectState>("specific days")

                    if (timeFieldState.validate() && freqFieldState.validate()) {
                        if (supportedDays.contains(freqFieldState.value)) {
                            if (daysSelectedState.validate()) {
                                readGoalsScreenViewModel.passMainScreenActions(
                                    action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                        currentPosition = readGoalsScreenUiState.value.currentPosition
                                    )
                                )
                            }
                        } else {
                            readGoalsScreenViewModel.passMainScreenActions(
                                action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                    currentPosition = readGoalsScreenUiState.value.currentPosition
                                )
                            )
                        }
                    }
                }

                2 -> {

                    val bookMonthsFieldState =
                        specificGoalsFormState.getState<TextFieldState>("books_month")
                    val timeChapterFieldState =
                        specificGoalsFormState.getState<TextFieldState>("time_chapter")
                    val periodChoiceState = specificGoalsFormState.getState<ChoiceState>("period")
                    val availableBooksState =
                        specificGoalsFormState.getState<ChoiceState>("available_books")
                    val periodDaysSelectState =
                        specificGoalsFormState.getState<SelectState>("period_days")


                    if (bookMonthsFieldState.validate() && availableBooksState.validate()) {
                        if (timeChapterFieldState.validate() && periodChoiceState.validate()) {
                            if (supportedDays.contains(periodChoiceState.value)) {
                                if (periodDaysSelectState.validate()) {
                                    Toast.makeText(
                                        context,
                                        "saving with specific stated days",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    readGoalsScreenViewModel.passMainScreenActions(
                                        action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                            currentPosition = readGoalsScreenUiState.value.currentPosition
                                        )
                                    )
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "saving without specific days",
                                    Toast.LENGTH_SHORT
                                ).show()
                                readGoalsScreenViewModel.passMainScreenActions(
                                    action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                        currentPosition = readGoalsScreenUiState.value.currentPosition
                                    )
                                )
                            }
                        }
                    }
                }

                else -> {}
            }


        },
        onPreviousClicked = {
            readGoalsScreenViewModel.passMainScreenActions(
                action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MovePrevious(
                    currentPosition = readGoalsScreenUiState.value.currentPosition
                )
            )
        }
    )

    MaterialDialog(
        dialogState = cameraPermissionRationalDialogState,
        backgroundColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally ,
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

            val rationalText = if(!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
                //camera permission is fully denied by user
                "Camera permission is required for you to be able to take book cover images. Redirecting you to the settings screen to grant this permission."
            }else{
                "Camera permission is required for you to be able to take book cover images.Please grant this permission then try again."
            }

            Text(
                text = rationalText ,
                style = BookAppTypography.bodyMedium,
                textAlign = TextAlign.Center ,
                color = MaterialTheme.colorScheme.primary
            )

            RoundedBrownButton(
                label = "Grant permission.",
                onClick = {
                    if(!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
                        //camera permission is fully denied by user
                        Toast.makeText(context, "Sending user to settings screen", Toast.LENGTH_SHORT).show()
                    }else{
                        launcher.launch(Manifest.permission.CAMERA)
                    }
                    cameraPermissionRationalDialogState.hide()
                }
            )
            Spacer(modifier = Modifier.height(20.dp))

        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "ReadGoalScreen", showBackground = true)
fun StatelessReadGoalScreen(
    context: Context = LocalContext.current,
    currentReadFormState: FormState<TextFieldState> = FormState(fields = listOf()),
    overallGoalsFormState: FormState<BaseState<out Any>> = FormState(fields = listOf()),
    specificGoalsFormState: FormState<BaseState<out Any>> = FormState(fields = listOf()),
    uiState: ReadGoalsScreenState = ReadGoalsScreenState(),
    imageSelectorState: ImageSelectorUiState = ImageSelectorUiState(),
    alertSwitchState: Boolean = false,
    popBackStack: () -> Unit = {},
    onGoogleIconClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onImageSelectorClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
    onPreviousClicked: () -> Unit = {},
    onAlertSwitchChecked: (Boolean) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        StandardToolBar(
            navActions = {
                //control visibility depending on where we are
                if (uiState.currentPosition == 0) {
                    Button(
                        onClick = {
                            onGoogleIconClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google_icon_24),
                            contentDescription = "google icon for searching books",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        )
                    }
                }

            },
            navigate = {
                //navigate back to home screen
                popBackStack()
            }
        ) {
            val toolBarTitle =
                if (uiState.currentPosition > 0) {
                    "Set goals"
                } else {
                    "Add your current read"
                }
            Text(
                text = toolBarTitle,
                style = BookAppTypography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.weight(1f)
        ) {

            androidx.compose.animation.AnimatedVisibility(
                visible = uiState.currentPosition == 0,
                enter = fadeIn() + slideInHorizontally { if (uiState.currentPosition > uiState.previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (uiState.currentPosition > uiState.previousPosition) -it else it }
            ) {
                CurrentReadForm(
                    currentReadFormState = currentReadFormState,
                    onSaveBookClicked = {
                        onSaveClicked()
                    },
                    imageSelectorState = imageSelectorState,
                    imageSelectorClicked = {
                        onImageSelectorClicked()
                    }
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = uiState.currentPosition == 1,
                enter = fadeIn() + slideInHorizontally { if (uiState.currentPosition > uiState.previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (uiState.currentPosition > uiState.previousPosition) -it else it }

            ) {
                OverallGoalsForm(
                    overallGoalsFormState = overallGoalsFormState,
                    alertSwitchState = alertSwitchState,
                    onAlertSwitchChecked = { status ->
                        onAlertSwitchChecked(status)
                    }
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = uiState.currentPosition == 2,
                enter = fadeIn() + slideInHorizontally { if (uiState.currentPosition > uiState.previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (uiState.currentPosition > uiState.previousPosition) -it else it }

            ) {
                SpecificGoalsForm(
                    specificGoalsFormState = specificGoalsFormState
                )
            }
        }

        BottomNextPreviousButtons(
            modifier = Modifier.padding(16.dp),
            currentPosition = uiState.currentPosition,
            onNextClicked = {
                onNextClicked()
            },
            onPreviousClicked = {
                onPreviousClicked()
            }
        )

    }


}


//camera permissions




