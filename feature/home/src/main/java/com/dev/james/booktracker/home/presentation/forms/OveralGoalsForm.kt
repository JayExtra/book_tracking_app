package com.dev.james.booktracker.home.presentation.forms


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.WeekDaySelectorComponent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.components.AlertSetupComponent
import com.dev.james.booktracker.home.presentation.components.DropDownComponent
import com.dev.james.booktracker.home.presentation.components.SwitchComponent
import com.dev.james.booktracker.home.presentation.components.TimerSelectorComponent
import com.dsc.form_builder.BaseState
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.SelectState
import com.dsc.form_builder.TextFieldState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerColors
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(name = "OverallGoalsForm", showBackground = true)
fun OverallGoalsForm(
    modifier: Modifier = Modifier,
    overallGoalsFormState: FormState<BaseState<out Any>> = FormState(fields = listOf()),
    alertSwitchState: Boolean = false,
    onAlertSwitchChecked: (Boolean) -> Unit = {},
    startDialPickerDialog: () -> Unit = {},
    startAlarmPickerDialog: () -> Unit = {}
) {

    var showTimerTimePickerDialog by remember {
        mutableStateOf(false)
    }

    val minTimeField = overallGoalsFormState.getState<TextFieldState>(name = "time")

    if (showTimerTimePickerDialog) {
        TimerTimePickerDialog(
            onDismiss = {
                showTimerTimePickerDialog = false
            } ,
            onSet = { selectedTime ->
                minTimeField.change(selectedTime)
            }
        )
    }


    var pickedTime by remember {
        mutableStateOf(LocalTime.now())
    }

    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm a")
                .format(pickedTime)
        }
    }

    val timeDialogState = rememberMaterialDialogState()





    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {

        Text(
            text = "Overall",
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
        )


        Text(
            text = "In a day , I want to read for at least.",
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.labelMedium,
            color = if (minTimeField.hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {

            OutlinedTextField(
                value = minTimeField.value,
                onValueChange = {},
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(0.dp),
                        //if error is available , change the color of border to error color
                        color = if (minTimeField.hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                    )
                    .weight(1f),

                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    errorCursorColor = MaterialTheme.colorScheme.error
                ),
                textStyle = BookAppTypography.bodyMedium,
                shape = RoundedCornerShape(0.dp),
                //adjust error depending if error is available
                isError = minTimeField.hasError,
                readOnly = true
            )

            Spacer(modifier = Modifier.width(20.dp))

            RoundedBrownButton(
                label = "Set",
                icon = R.drawable.baseline_access_alarm_24,
                onClick = {
                    //launch alarm picker dialog
                    showTimerTimePickerDialog = true

                },
                cornerRadius = 10.dp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val dropDownItems = listOf<String>(
            "Every day",
            "Every day except",
            "Weekend only",
            "Weekdays",
            "Select specific days"
        )

        val frequencyDropDownState =
            overallGoalsFormState.getState<ChoiceState>(name = "frequency field")

        //will show whether or not if specific days are shown
        val supportedDays = listOf(
            "Select specific days", "Every day except"
        )
        val daysSelectorState = overallGoalsFormState.getState<SelectState>(name = "specific days")


        DropDownComponent(
            label = "For how long do you want this goal to run?",
            modifier = Modifier.fillMaxWidth(),
            selectedText = frequencyDropDownState.value,
            canUserFill = false,
            dropDownItems = dropDownItems,
            onListItemSelected = {
                if (!supportedDays.contains(it)) {
                    daysSelectorState.value.clear()
                }
                //show items
                frequencyDropDownState.change(it)
            },
            placeHolderText = "",
            hasError = frequencyDropDownState.hasError
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = supportedDays.contains(frequencyDropDownState.value),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + slideOutVertically(
                animationSpec = tween(durationMillis = 500),
                targetOffsetY = { -it / 2 }
            ),
            enter = fadeIn(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + slideInVertically(
                animationSpec = tween(durationMillis = 500),
                initialOffsetY = { -it / 2 }
            )
        ) {
            WeekDaySelectorComponent(
                modifier = Modifier.fillMaxWidth(),
                onDaySelected = { day ->
                    //update selected days list
                    if (daysSelectorState.value.contains(day)) {
                        daysSelectorState.unselect(day)
                    } else {
                        daysSelectorState.select(day)
                    }
                },
                selectedDays = daysSelectorState.value
            )
        }



        Spacer(modifier = Modifier.height(20.dp))

        SwitchComponent(
            isChecked = alertSwitchState,
            onCheckChanged = { status ->
                //update checked state
                onAlertSwitchChecked(status)
            }
        )

        //show this section whether or when the switch is on or off
        val noteFieldState = overallGoalsFormState.getState<TextFieldState>("alert note")

        AnimatedVisibility(
            visible = alertSwitchState,
            exit = fadeOut(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + slideOutVertically(
                animationSpec = tween(durationMillis = 500),
                targetOffsetY = { -it / 2 }
            ),
            enter = fadeIn(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + slideInVertically(
                animationSpec = tween(durationMillis = 500),
                initialOffsetY = { -it / 2 }
            )
        ) {

            AlertSetupComponent(
                noteText = noteFieldState.value,
                onNoteFieldChange = { note ->
                    noteFieldState.change(note)
                } ,
                onTimeSelectorClicked = {
                    timeDialogState.show()
                } ,
                timeSet = formattedTime
            )

        }


    }

    MaterialDialog(
        dialogState = timeDialogState ,
        buttons = {
            positiveButton(text = "Set"){

            }
            negativeButton(text = "Close"){

            }
        }
    ) {
        timepicker(
            initialTime =  LocalTime.now() ,
            title = "Set your alert time" ,
        ){ time ->
            pickedTime = time
        }

    }


}

@Composable
fun TimerTimePickerDialog(
    onSet : (String) -> Unit = {} ,
    onDismiss: () -> Unit = {}
) {
    val context  = LocalContext.current
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        TimerSelectorComponent(
            onDismiss = {
                onDismiss()
            } ,
            onSet = { selectedTime ->
                onSet(selectedTime)
            }
        )
    }
}


