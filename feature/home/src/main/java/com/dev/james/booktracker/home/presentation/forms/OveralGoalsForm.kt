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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dev.james.booktracker.compose_ui.ui.components.CounterComponent
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.WeekDaySelectorComponent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.components.AlertSetupComponent
import com.dev.james.booktracker.home.presentation.components.DropDownComponent
import com.dev.james.booktracker.home.presentation.components.SelectedTime
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
@Preview(name = "OverallGoalsForm", showBackground = true)
fun OverallGoalsForm(
    modifier: Modifier = Modifier,
    overallGoalsFormState: FormState<BaseState<out Any>> = FormState(fields = listOf()),
    alertSwitchState: Boolean = false,
    onAlertSwitchChecked: (Boolean) -> Unit = {},
    startDialPickerDialog: () -> Unit = {},
    startAlarmPickerDialog: () -> Unit = {} ,
    onSaveGoal : () -> Unit = {}
) {

    val booksFieldState = overallGoalsFormState.getState<TextFieldState>(
        name = "books_month"
    )


    val context = LocalContext.current


    var showTimerTimePickerDialog by remember {
        mutableStateOf(false)
    }

    val minTimeField = overallGoalsFormState.getState<TextFieldState>(name = "time")

    if (showTimerTimePickerDialog) {
        TimerTimePickerDialog(
            onDismiss = {
                showTimerTimePickerDialog = false
            },
            onSet = { selectedTime ->
                when {
                    selectedTime.hours > 0 && selectedTime.minutes == 0 -> {
                        minTimeField.change("${selectedTime.hours}hrs")
                        Toast.makeText(context , minTimeField.value , Toast.LENGTH_SHORT ).show()
                    }
                    selectedTime.hours == 0 && selectedTime.minutes > 0 -> {
                        minTimeField.change("${selectedTime.minutes}min")
                        Toast.makeText(context , minTimeField.value , Toast.LENGTH_SHORT ).show()
                    }
                    selectedTime.hours > 0 && selectedTime.minutes > 0 -> {
                        minTimeField.change("${selectedTime.hours}hrs ${selectedTime.minutes}min")
                        Toast.makeText(context , minTimeField.value , Toast.LENGTH_SHORT ).show()
                    }
                    else -> {
                        minTimeField.change("no time set")
                    }
                }
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

    var shouldShowCounterDialog  by remember {
        mutableStateOf(false)
    }


    if(shouldShowCounterDialog){
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true ,
                dismissOnClickOutside = false ,
                usePlatformDefaultWidth = false
            ) ,
            onDismissRequest = { }
        ) {
            CounterComponent(
                onSet = { count ->
                    booksFieldState.change(count.toString())
                } ,
                onDismiss = {
                    shouldShowCounterDialog = false
                }
            )
        }
    }



    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
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
                selectedDays = daysSelectorState.value ,
                hasError = daysSelectorState.hasError
            )
        }



        Spacer(modifier = Modifier.height(20.dp))

        //add number of books section here
        Column(
            modifier = Modifier.fillMaxWidth() ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "How many books do you want to read in a month?.",
                modifier = Modifier.fillMaxWidth(),
                style = BookAppTypography.labelMedium,
                color = if (booksFieldState.hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = booksFieldState.value,
                    onValueChange = {
                        //  booksFieldState.change(update = it)
                    },
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            shape = RoundedCornerShape(0.dp),
                            //if error is available , change the color of border to error color
                            color = if (booksFieldState.hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                        )
                        .weight(1f),

                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        errorCursorColor = MaterialTheme.colorScheme.error
                    ),
                    textStyle = BookAppTypography.bodyMedium,
                    shape = RoundedCornerShape(0.dp),
                    //adjust error depending if error is available
                    isError = booksFieldState.hasError,
                    singleLine = true,
                    readOnly = true
                )
                Spacer(modifier = Modifier.width(20.dp))

                RoundedBrownButton(
                    label = "Set",
                    onClick = {
                        //launch book count dialog
                        shouldShowCounterDialog = true
                    },
                    cornerRadius = 10.dp
                )

            }
        }


        Spacer(modifier = Modifier.height(20.dp))



        val alertSwitchFieldState : ChoiceState = overallGoalsFormState.getState<ChoiceState>("alert_switch")

        SwitchComponent(
            isChecked = alertSwitchFieldState.value == "Yes",
            onCheckChanged = { status ->
                //update checked state
               // onAlertSwitchChecked(status)
                if(status){
                    alertSwitchFieldState.change("Yes")
                }else {
                    alertSwitchFieldState.change("No")
                }

            }
        )

        //show this section whether or when the switch is on or off
        val noteFieldState = overallGoalsFormState.getState<TextFieldState>("alert note")

        AnimatedVisibility(
            visible = alertSwitchFieldState.value == "Yes",
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
                },
                onTimeSelectorClicked = {
                    timeDialogState.show()
                },
                timeSet = formattedTime
            )

        }

        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = { onSaveGoal() } ,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "save goal" , style = BookAppTypography.labelMedium)
        }


    }

    val timePickedState = overallGoalsFormState.getState<ChoiceState>("alert_dialog_time")

    MaterialDialog(
        dialogState = timeDialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        buttons = {
            positiveButton(text = "Set") {
                timePickedState.change(formattedTime)
                Toast.makeText(context , formattedTime , Toast.LENGTH_SHORT).show()
            }
            negativeButton(text = "Close") {

            }
        }
    ) {
        timepicker(
            initialTime = LocalTime.NOON,
            title = "Select time",
            colors = TimePickerDefaults
                .colors(
                    activeBackgroundColor = MaterialTheme.colorScheme.secondary ,
                    selectorColor = MaterialTheme.colorScheme.secondary ,
                )

        ) { time ->
            pickedTime = time
        }
    }


}

@Composable
fun TimerTimePickerDialog(
    onSet: (SelectedTime) -> Unit = {
        SelectedTime(
            hours = 0,
            minutes = 0,
        )
    },
    onDismiss: () -> Unit = {}
) {
    //val context  = LocalContext.current

    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        TimerSelectorComponent(
            onDismiss = {
                onDismiss()
            },
            onSet = { selectedTime ->
                onSet(selectedTime)
            }
        )
    }
}


