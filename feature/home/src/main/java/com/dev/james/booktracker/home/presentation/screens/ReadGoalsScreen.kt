package com.dev.james.booktracker.home.presentation.screens

import android.widget.Toast
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.components.BottomNextPreviousButtons
import com.dev.james.booktracker.home.presentation.components.TextFieldComponent
import com.dev.james.booktracker.home.presentation.forms.CurrentReadForm
import com.dev.james.booktracker.home.presentation.forms.OverallGoalsForm
import com.dev.james.booktracker.home.presentation.viewmodels.ImageSelectorUiState
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenState
import com.dsc.form_builder.BaseState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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


    StatelessReadGoalScreen(
        currentReadFormState = currentReadFormState,
        uiState = readGoalsScreenUiState.value,
        overallGoalsFormState = overallGoalsFormState,
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
                //start saving process to db
                Toast.makeText(
                    context,
                    "Form is properly filled , saving data...",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        },
        onImageSelectorClicked = {

            /*currentSelectedImage = "some image selected"
            imageSelectedError = currentSelectedImage.isBlank()*/

            //1.update selector state to show circular progress
            readGoalsScreenViewModel.passAddReadFormAction(
                ReadGoalsScreenViewModel.AddReadFormUiActions.LaunchImagePicker
            )

            //2.launch image picker
            //this current implementation simulates the image picking action will replace
            //with actual image picker implementation
            coroutineScope.launch {
                delay(5000L)

                readGoalsScreenViewModel.passAddReadFormAction(
                    ReadGoalsScreenViewModel.AddReadFormUiActions
                        .ImageSelected(imageUri = "some image uri")
                )

            }


        },
        onAlertSwitchChecked = { status ->
            checkedState = status
        },

        onNextClicked = {
            readGoalsScreenViewModel.passMainScreenActions(
                action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                    currentPosition = readGoalsScreenUiState.value.currentPosition
                )
            )
        },
        onPreviousClicked = {
            readGoalsScreenViewModel.passMainScreenActions(
                action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MovePrevious(
                    currentPosition = readGoalsScreenUiState.value.currentPosition
                )
            )
        }
    )
}

@Composable
@Preview(name = "ReadGoalScreen", showBackground = true)
fun StatelessReadGoalScreen(
    currentReadFormState: FormState<TextFieldState> = FormState(fields = listOf()),
    overallGoalsFormState: FormState<BaseState<out Any>> = FormState(fields = listOf()),
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



