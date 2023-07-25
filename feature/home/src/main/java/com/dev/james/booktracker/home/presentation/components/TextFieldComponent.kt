package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    hint: String = "",
    isSingleLine : Boolean = false,
    startingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClicked: () -> Unit = {},
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
            singleLine = isSingleLine,
            modifier = modifier
                .padding(top = 8.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(0.dp),
                    //if error is available , change the color of border to error color
                    color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                ),
            placeholder = {
                if (hint.isNotBlank()) {
                    Text(
                        text = hint,
                        style = BookAppTypography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            textStyle = BookAppTypography.bodyMedium,
            shape = RoundedCornerShape(0.dp),
            //adjust error depending if error is available
            isError = hasError,
            leadingIcon = {
                if (startingIcon != null) {
                    Icon(
                        imageVector = startingIcon,
                        contentDescription = "text field leading icon",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            trailingIcon = {
                if (text.isNotBlank()) {
                    if (trailingIcon != null) {
                        IconButton(
                            onClick = {
                                onTrailingIconClicked()
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = "text field trailing icon",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

            }
        )

    }

}