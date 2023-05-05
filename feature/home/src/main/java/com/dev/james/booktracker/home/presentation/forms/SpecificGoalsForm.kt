package com.dev.james.booktracker.home.presentation.forms

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
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
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

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
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

    }
}