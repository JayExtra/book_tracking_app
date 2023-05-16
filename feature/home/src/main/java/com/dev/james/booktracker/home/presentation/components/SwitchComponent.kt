package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography


@Composable
@Preview
fun SwitchComponent(
    isChecked: Boolean = false,
    onCheckChanged: (Boolean) -> Unit = {}
) {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Do you wish to get alerts?",
            style = BookAppTypography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )


        Switch(

            checked = isChecked,
            onCheckedChange = { status ->
                //update check state
                onCheckChanged(status)
            },
            thumbContent = {
                if (isChecked) {
                    Text(
                        text = "Yes",
                        style = BookAppTypography.labelSmall
                    )
                } else {
                    Text(
                        text = "No",
                        style = BookAppTypography.labelSmall
                    )
                }

            }

        )

    }

}