package com.dev.james.booktracker.home.presentation.forms

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.WeekDaySelectorComponent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.components.DropDownComponent
import com.dsc.form_builder.BaseState
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.SelectState
import com.dsc.form_builder.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dev.james.booktracker.compose_ui.ui.components.CounterComponent
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
@Preview("SpecificGoalsForm", showBackground = true)
fun SpecificGoalsForm(
    specificGoalsFormState: FormState<BaseState<out Any>> = FormState(emptyList())
) {

    val booksFieldState = specificGoalsFormState.getState<TextFieldState>(
        name = "books_month"
    )

    val availableBookFieldState = specificGoalsFormState.getState<ChoiceState>(
        name = "available_books"
    )

    val chapterHrsFieldState = specificGoalsFormState.getState<ChoiceState>(
        name = "chapter_hours"
    )

    val timeOrChapterFieldState = specificGoalsFormState.getState<TextFieldState>(
        name = "time_chapter"
    )

    val frequencyDropDownState = specificGoalsFormState.getState<ChoiceState>(
        name = "period"
    )

    val selectedDaysState = specificGoalsFormState.getState<SelectState>(
        name = "period_days"
    )

    var shouldShowCounterDialog  by remember {
        mutableStateOf(false)
    }

    var shouldShowTimerPickerDialog by remember {
        mutableStateOf(false)
    }

    var isCalledByChapter by remember {
        mutableStateOf(false)
    }

    //isCalledByChapter = chapterHrsFieldState.value == "By chapters"


    /*
    * counter dialog
    * */
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
                    if (isCalledByChapter){
                        if(count == 1){
                            timeOrChapterFieldState.change("$count chapter")
                        }else {
                            timeOrChapterFieldState.change("$count chapters")
                        }

                    }else {
                        booksFieldState.change(count.toString())
                    }
                } ,
                onDismiss = {
                    shouldShowCounterDialog = false
                    isCalledByChapter = false
                }
            )
        }
    }

    if (shouldShowTimerPickerDialog){
       Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true ,
                dismissOnClickOutside = false ,
                usePlatformDefaultWidth = false
            ) ,
            onDismissRequest = { }
        ) {
           TimerTimePickerDialog(
               onSet = { selectedTime ->
                   when {
                       selectedTime.hours > 0 && selectedTime.minutes == 0 -> {
                           timeOrChapterFieldState.change("${selectedTime.hours} hrs")
                       }

                       selectedTime.hours == 0 && selectedTime.minutes > 0 -> {
                           timeOrChapterFieldState.change("${selectedTime.minutes} min")
                       }

                       selectedTime.hours > 0 && selectedTime.minutes > 0 -> {
                           timeOrChapterFieldState.change("${selectedTime.hours} hrs ${selectedTime.minutes} min")
                       }

                       else -> {
                           timeOrChapterFieldState.change("no time set")
                       }
                   }
               } ,
               onDismiss = {
                   shouldShowTimerPickerDialog = false
               }
           )
       }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {

        Text(
            text = "Specific",
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )


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

        Text(
            text = "Add a goal for a book you are reading.",
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
        )

        //dropdown items will be replaced by books cached locally
        val dropDownItems = listOf("River Between", "Think Big")

        DropDownComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Available books",
            canUserFill = false,
            placeHolderText = "please select a book" ,
            dropDownItems = dropDownItems,
            onListItemSelected = { book ->
                //select book
                availableBookFieldState.change(update = book)
            },
            selectedText = availableBookFieldState.value ,
            hasError = availableBookFieldState.hasError
        )

        AnimatedVisibility(
            visible = availableBookFieldState.value.isNotBlank(),
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
            BookGoalsSection(
                chapterHoursDropdownState = chapterHrsFieldState,
                timeChapterFieldState = timeOrChapterFieldState ,
                frequencyDropDownFieldState = frequencyDropDownState ,
                daysSelectorState = selectedDaysState ,
                showChapterDialog = { isChapter ->
                        if(isChapter){
                            shouldShowCounterDialog = true
                            isCalledByChapter = true
                        }else {
                            shouldShowTimerPickerDialog = true
                        }
                }
            )
        }

    }
}

@Composable
fun BookGoalsSection(
    chapterHoursDropdownState: ChoiceState,
    timeChapterFieldState: TextFieldState ,
    frequencyDropDownFieldState : ChoiceState ,
    daysSelectorState : SelectState ,
    showChapterDialog : (Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        val dropDownItems = listOf("By hours", "By chapters")

        DropDownComponent(
            label = "Chapter or hours",
            canUserFill = false,
            placeHolderText = chapterHoursDropdownState.initial,
            dropDownItems = dropDownItems,
            onListItemSelected = { selection ->
                chapterHoursDropdownState.change(update = selection)
            },
            modifier = Modifier.fillMaxWidth(),
            selectedText = chapterHoursDropdownState.value
        )
        //update according to what is selected either hours or chapters
        val chapterHrsTitleText =
            if (chapterHoursDropdownState.value == "By chapters") {
                "In a day I want to read a minimum of."
            } else {
                "In a day , I want to read for at least."
            }

        Text(
            text = chapterHrsTitleText ,
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.labelMedium,
            color = if(timeChapterFieldState.hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
        )


        BookTimeOrChapterSection(
            isChapter = chapterHoursDropdownState.value == "By chapters",
            timeChapterFieldState = timeChapterFieldState ,
            showDialog = { isChapterDialog ->
                /*Toast.makeText(
                    context, "chapter hours choice state => ${chapterHoursDropdownState.value} " +
                            "chapter hours text field state => ${timeChapterFieldState.value}", Toast.LENGTH_SHORT).show()*/
                showChapterDialog(isChapterDialog)
            }
        )

        val dropDownList = listOf<String>(
            "Every day",
            "Every day except",
            "Weekend only",
            "Weekdays",
            "Select specific days"
        )

        val supportedDays = listOf(
            "Select specific days", "Every day except"
        )

        DropDownComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "How long do you wish this goal to run?",
            placeHolderText = frequencyDropDownFieldState.initial,
            canUserFill = false,
            dropDownItems = dropDownList,
            onListItemSelected = { frequency ->
                if(!supportedDays.contains(frequencyDropDownFieldState.value)){
                    daysSelectorState.value.clear()
                }
                frequencyDropDownFieldState.change(update = frequency)
            } ,
            selectedText = frequencyDropDownFieldState.value ,
            hasError = frequencyDropDownFieldState.hasError
        )

        AnimatedVisibility(
            visible = supportedDays.contains(frequencyDropDownFieldState.value),
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
                hasError = daysSelectorState.hasError ,
                onDaySelected = {day ->

                    if(daysSelectorState.value.contains(day)){
                        daysSelectorState.unselect(day)
                    }else{
                        daysSelectorState.select(selectValue = day )
                    }

                } ,
                selectedDays = daysSelectorState.value
            )

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTimeOrChapterSection(
    context : Context = LocalContext.current,
    isChapter: Boolean = false,
    timeChapterFieldState: TextFieldState ,
    showDialog : (Boolean) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        OutlinedTextField(
            value = timeChapterFieldState.value,
            onValueChange = {},
            modifier = Modifier
                .padding(top = 0.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(0.dp),
                    //if error is available , change the color of border to error color
                    color = if (timeChapterFieldState.hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )
                .weight(1f),

            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            textStyle = BookAppTypography.bodyMedium,
            shape = RoundedCornerShape(0.dp),
            //adjust error depending if error is available
            isError = timeChapterFieldState.hasError,
            singleLine = true,
            readOnly = true
        )

        Spacer(modifier = Modifier.width(20.dp))

        RoundedBrownButton(
            label = "Set",
            icon = if (isChapter) 0 else R.drawable.baseline_access_alarm_24,
            onClick = {
                //launch alarm picker dialog or dial picker
                      if(isChapter){
                         /* Toast.makeText(context , "Chapter option" , Toast.LENGTH_SHORT)
                              .show()*/
                          showDialog(true)
                      }else{
                         /* Toast.makeText(context , "Hours option" , Toast.LENGTH_SHORT)
                              .show()*/
                          showDialog(false)
                      }
            },
            cornerRadius = 10.dp
        )
    }
}