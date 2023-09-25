package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.ui.components.OutlinedTextFieldComponent
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R

@Composable
@Preview
fun AlertSetupComponent(
    noteText: String = "",
    hasError: Boolean = false,
    timeSet: String = "",
    onNoteFieldChange: (String) -> Unit = {},
    onTimeSelectorClicked: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Add note",
            onTextChanged = { note ->
                //update text action
                onNoteFieldChange(note)
            },
            text = noteText
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Time Set",
            style = BookAppTypography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = timeSet,
                style = BookAppTypography.headlineLarge,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 32.sp
            )

            RoundedBrownButton(
                icon = R.drawable.baseline_access_alarm_24,
                label = "Set",
                onClick = {
                    //open
                    onTimeSelectorClicked()
                },
                cornerRadius = 10.dp
            )

        }

    }
}
