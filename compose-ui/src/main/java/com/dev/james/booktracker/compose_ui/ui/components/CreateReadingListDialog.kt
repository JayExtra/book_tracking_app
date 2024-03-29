package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography


@Composable
@Preview()
fun CreateReadingListDialog(
    // imageSelectorUiState: ImageSelectorUiState = ImageSelectorUiState(),
    onSaveReadingList: (String, String) -> Unit = { _, _ -> },
    onAddImage: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {

    var nameText by rememberSaveable {
        mutableStateOf("")
    }
    var nameFieldError by rememberSaveable {
        mutableStateOf(false)
    }
    var descriptionText by rememberSaveable {
        mutableStateOf("")
    }
    var descriptionFieldError by rememberSaveable {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(4.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.onBackground
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_playlist_add),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                }
                //Spacer(modifier = Modifier.height(6.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "create new reading list",
                    style = BookAppTypography.labelLarge,
                    textAlign = TextAlign.Center
                )
                /*  Spacer(modifier = Modifier.height(10.dp))

                  ImageSelectorComponent(
                      imageSelectorState = ImageSelectorUiState(),
                      modifier = Modifier.fillMaxWidth(),
                      width = 0.dp ,
                      height = 140.dp ,
                     *//* isCircular = true,
                    size = 100.dp,*//*
                    onSelect = {
                        onAddImage()
                    },
                    onClear = {

                    }
                )*/

                OutlinedTextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    hint = "name",
                    text = nameText,
                    onTextChanged = {
                        nameText = it
                    },
                    isSingleLine = true,
                    hasError = nameFieldError,
                    roundedCornerSize = 5.dp
                )

                OutlinedTextFieldComponent(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    hint = "description",
                    text = descriptionText,
                    onTextChanged = {
                        descriptionText = it
                    },
                    isSingleLine = false,
                    hasError = descriptionFieldError,
                    roundedCornerSize = 5.dp ,
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    // .padding(5.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    OutlinedButton(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = { onDismissRequest() }
                        /*colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )*/
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(text = "cancel", style = BookAppTypography.labelMedium)
                    }

                    Button(
                        modifier = Modifier.wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            /*save reading list*/
                            if (nameText.isBlank()) {
                                nameFieldError = true
                            } else if (descriptionText.isBlank()) {
                                descriptionFieldError = true
                            } else {
                                onSaveReadingList(
                                    //imageSelectorUiState.imageSelectedUri.toString(),
                                    nameText,
                                    descriptionText
                                )
                            }
                        })
                    {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.background
                        )
                        Text(text = "create list", style = BookAppTypography.labelMedium)
                    }
                }


            }

        }


    }

}