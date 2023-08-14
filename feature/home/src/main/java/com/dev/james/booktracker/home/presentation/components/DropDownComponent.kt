package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
@Preview("DropDownComponent")
fun DropDownComponent(
    modifier: Modifier = Modifier,
    selectedText: String = "",
    hasError: Boolean = false,
    label: String = "",
    placeHolderText: String = "",
    dropDownItems: List<String?> = listOf(),
    onListItemSelected: (String) -> Unit = {},
    canUserFill: Boolean = false
) {
    /*
    component for a dropdown menu
    */

    var isExpanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        if (label.isNotBlank()) {
            Text(
                modifier = if (label.length > 15) Modifier.fillMaxWidth() else Modifier,
                text = if (hasError) "$label*" else label,
                style = BookAppTypography.labelMedium,
                color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {
                /*if (!canUserFill) {
                    keyboardController?.hide()
                }*/
                isExpanded = it
            },
            modifier = modifier
        ) {

            TextField(
                value = selectedText.ifBlank { placeHolderText },
                readOnly = !canUserFill,
                onValueChange = {
                    if (canUserFill) {
                        onListItemSelected(it)
                    }
                },
                modifier = modifier
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(0.dp),
                        color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                    )
                    .menuAnchor(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    cursorColor = if (canUserFill) MaterialTheme.colorScheme.secondary else Color.Transparent
                ),
                shape = RoundedCornerShape(0.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true

            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                dropDownItems.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            if (item != null) {
                                onListItemSelected(item)
                            }
                            isExpanded = false
                        },
                        text = {
                            if (item != null) {
                                Text(text = item, style = BookAppTypography.bodySmall)
                            }
                        }
                    )
                }
            }

        }
    }
}
