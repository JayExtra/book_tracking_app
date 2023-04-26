package com.dev.james.booktracker.home.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.components.StatefulRoundOutlineButton
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.ramcosta.composedestinations.annotation.Destination
import timber.log.Timber

@Composable
@Destination
fun ReadGoalScreen(
    homeNavigator: HomeNavigator
) {

    StatelessReadGoalScreen(
        popBackStack = {
            homeNavigator.openHomeScreen()
        }
    )
}

@Composable
@Preview(name = "ReadGoalScreen", showBackground = true)
fun StatelessReadGoalScreen(
    popBackStack: () -> Unit = {},
    onGoogleIconClicked: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        StandardToolBar(
            navActions = {
                //control visibility depending on where we are
                Button(
                    onClick = {
                        onGoogleIconClicked()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.google_icon_24),
                        contentDescription = "google icon for searching books",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                }
            },
            navigate = {
                //navigate back to home screen
                popBackStack()
            }
        ) {
            Text(
                text = "Add your current read",
                style = BookAppTypography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        CurrentReadForm()

        Spacer(modifier = Modifier.height(24.dp))

        BottomNextPreviousButtons(
            modifier = Modifier.padding(16.dp) ,
            currentPosition = 0
        )




    }
}

@Composable
@Preview("CurrentReadForm")
fun CurrentReadForm(
    //will take in form state
    onSaveBookClicked: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {

        ImageSelectorComponent()

        Spacer(modifier = Modifier.height(32.dp))

        TextFieldComponent(
            label = "Title"
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .padding(start = 16.dp)
        )

        TextFieldComponent(
            label = "Author"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val chapterCount = mutableListOf<String>()
            for (chapter in 1..50) {
                chapterCount.add(
                    chapter.toString()
                )
            }

            DropDownComponent(
                label = "Chapters",
                dropDownItems = chapterCount.toList()
            ) { chapter ->
                Timber.tag("ReadGoalsScreen").d(chapter)
                //update chapter count
            }

            Spacer(modifier = Modifier.width(30.dp))

            DropDownComponent(
                label = "Current chapter",
                dropDownItems = chapterCount.toList()
            ) { chapter ->
                Timber.tag("ReadGoalsScreen").d(chapter)
                //update current selected chapter
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldComponent(
            label = "Current chapter title"
        )

        Spacer(modifier = Modifier.height(18.dp))

        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { },
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = "Add book",
                style = BookAppTypography.labelLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }

}

@Composable
@Preview("ImageSelectorComponent")
fun ImageSelectorComponent(
    showProgressBar: Boolean = false,
    showPlaceHolder: Boolean = true,
    //could be image could be uri , subject to change
    selectedImage: String = "",
    onSelect: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(158.dp)
            .height(191.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                //starts the image picking flow
                onSelect()
            }
    ) {

        if (selectedImage.isNotBlank()) {
            //will replace with coil
            Image(
                painterResource(id = R.drawable.image_placeholder_24),
                contentDescription = "taken book image",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )
        }

        if (showPlaceHolder) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(id = R.drawable.add_a_photo_24),
                    contentDescription = "Add image placeholder",
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp),
                    tint = MaterialTheme.colorScheme.secondary

                )
                Text(
                    text = "Add image",
                    style = BookAppTypography.labelLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        if (showProgressBar) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(name = "TextFieldComponent")
fun TextFieldComponent(
    text: String = "",
    label: String = "Some label",
    onTextChanged: (String) -> Unit = {}
) {
    //var currentText by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = BookAppTypography.labelMedium,
            modifier = Modifier.fillMaxWidth()
            //also adjust error color and text if error is available
        )

        OutlinedTextField(
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(0.dp),
                    //if error is available , change the color of border to error color
                    color = MaterialTheme.colorScheme.secondary
                )
                .fillMaxWidth(),

            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error
            ),
            textStyle = BookAppTypography.bodyMedium,
            shape = RoundedCornerShape(0.dp),
            //adjust error depending if error is available
            isError = true,
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview("DropDownComponent")
fun DropDownComponent(
    modifier: Modifier = Modifier,
    label: String = "some label",
    dropDownItems: List<String> = listOf(),
    onListItemSelected: (String) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }
    //var textFieldSize by remember { mutableStateOf(Size.Zero)}
    var selectedText by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = label,
            style = BookAppTypography.labelMedium
        )

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = Modifier.width(180.dp)
        ) {
            TextField(
                value = selectedText,
                onValueChange = { selectedText = it },
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(0.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    .menuAnchor(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                shape = RoundedCornerShape(0.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                dropDownItems.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            onListItemSelected(item)
                            selectedText = item
                            isExpanded = false
                        },
                        text = {
                            Text(text = item, style = BookAppTypography.bodySmall)
                        }
                    )
                }
            }

        }
    }
}


@Composable
@Preview("BottomButtonsSection" , showBackground = true)
fun BottomNextPreviousButtons(
    modifier: Modifier = Modifier,
    onNextClicked: () -> Unit = {},
    onPreviousClicked: () -> Unit = {},
    currentPosition: Int = 0
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (currentPosition > 0) Arrangement.SpaceBetween else Arrangement.End
        ) {

            AnimatedVisibility(
                visible = currentPosition > 0,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 100,
                        delayMillis = 10,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 100,
                        delayMillis = 10,
                        easing = FastOutSlowInEasing
                    )
                )
            ) {
                RoundedBrownButton(
                    label = "Previous",
                    cornerRadius = 8.dp
                ) {
                    onPreviousClicked()
                }
            }

            RoundedBrownButton(
                label = if (currentPosition == 1) "Finish" else "Next",
                cornerRadius = 8.dp
            ) {
                onNextClicked()
            }

        }


    }

}


