package com.dev.james.booktracker.home.presentation.forms

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.WeekDaySelectorComponent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.components.AlertSetupComponent
import com.dev.james.booktracker.home.presentation.components.DropDownComponent
import com.dev.james.booktracker.home.presentation.components.TextFieldComponent
import com.dsc.form_builder.TextFieldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview("SpecificGoalsForm", showBackground = true)
fun SpecificGoalsForm() {

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


        //val minTimeField = overallGoalsFormState.getState<TextFieldState>(name = "time")
        Text(
            text = "How many books do you want to read in a month?.",
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.labelMedium,
            color = /*if (minTimeField.hasError) MaterialTheme.colorScheme.error else*/ MaterialTheme.colorScheme.primary,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {

                    },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .border(
                            width = 2.dp,
                            shape = RoundedCornerShape(0.dp),
                            //if error is available , change the color of border to error color
                            color = /*if (minTimeField.hasError) MaterialTheme.colorScheme.error else*/ MaterialTheme.colorScheme.secondary
                        )
                        .weight(1f),

                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        errorCursorColor = MaterialTheme.colorScheme.error
                    ),
                    textStyle = BookAppTypography.bodyMedium,
                    shape = RoundedCornerShape(0.dp),
                    //adjust error depending if error is available
                   // isError = minTimeField.hasError,
                )

                Spacer(modifier = Modifier.width(20.dp))

                RoundedBrownButton(
                    label = "Set",
                    icon = R.drawable.baseline_access_alarm_24,
                    onClick = {
                        //launch alarm picker dialog
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
        val dropDownItems = listOf("River Between" , "Think Big")

        DropDownComponent(
            modifier = Modifier.fillMaxWidth() ,
            label = "Available books" ,
            canUserFill = false ,
            placeHolderText = "select a book" ,
            dropDownItems = dropDownItems ,
            onListItemSelected = { book ->
                //select book
            }
        )

        BookGoalsSection()
    }
}

@Composable
fun BookGoalsSection(){
    Column {

        val dropDownItems = listOf("By hours" , "By chapters")
        DropDownComponent(
            label = "Chapter or hours" ,
            canUserFill = false ,
            placeHolderText = "select option" ,
            dropDownItems = dropDownItems ,
            onListItemSelected = {

            } ,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        //update according to what is selected either hours or chapters
        Text(
            text = "In a day , I want to read for at least",
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
        )

       BookTimeOrChapterSection()

        Spacer(modifier = Modifier.height(16.dp))

        val dropDownList = listOf<String>(
            "Every day",
            "Every day except",
            "Weekend only",
            "Weekdays",
            "Select specific days"
        )

       DropDownComponent(
           modifier = Modifier.fillMaxWidth() ,
           label = "How long do you wish this goal to run?" ,
           placeHolderText = "select period" ,
           canUserFill = false ,
           dropDownItems = dropDownList ,
           onListItemSelected = {

           }
       )

        Spacer(modifier = Modifier.height(16.dp))

        WeekDaySelectorComponent(
            modifier = Modifier.fillMaxWidth() ,
            onDaySelected = {

            }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTimeOrChapterSection(
    isChapter : Boolean = false
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {

        OutlinedTextField(
            value = "",
            onValueChange = {

            },
            modifier = Modifier
                .padding(top = 8.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(0.dp),
                    //if error is available , change the color of border to error color
                    color = /*if (minTimeField.hasError) MaterialTheme.colorScheme.error else*/ MaterialTheme.colorScheme.secondary
                )
                .weight(1f),

            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            textStyle = BookAppTypography.bodyMedium,
            shape = RoundedCornerShape(0.dp),
            //adjust error depending if error is available
            isError = /*minTimeField.hasError*/ false,
        )

        Spacer(modifier = Modifier.width(20.dp))

        RoundedBrownButton(
            label = "Set",
            icon = if(isChapter) R.drawable.baseline_access_alarm_24 else 0,
            onClick = {
                //launch alarm picker dialog or dial picker
            },
            cornerRadius = 10.dp
        )
    }
}