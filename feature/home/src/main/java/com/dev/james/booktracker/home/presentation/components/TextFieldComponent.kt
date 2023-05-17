package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(name = "TextFieldComponent")
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    text: String = "",
    hasError: Boolean = false,
    label: String = "",
    onTextChanged: (String) -> Unit = {}
) {
    //var currentText by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (label.isNotBlank()) {
            Text(
                text = if (hasError) "$label*" else label,
                style = BookAppTypography.labelMedium,
                color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            modifier = modifier
                .padding(top = 8.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(0.dp),
                    //if error is available , change the color of border to error color
                    color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                ),

            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            textStyle = BookAppTypography.bodyMedium,
            shape = RoundedCornerShape(0.dp),
            //adjust error depending if error is available
            isError = hasError,
        )

    }

}