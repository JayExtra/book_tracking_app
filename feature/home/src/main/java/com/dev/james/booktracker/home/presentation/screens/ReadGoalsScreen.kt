package com.dev.james.booktracker.home.presentation.screens

import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.components.WeekDaySelectorComponent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.viewmodels.ImageSelectorUiState
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
@Destination
fun ReadGoalScreen(
    homeNavigator: HomeNavigator,
    readGoalsScreenViewModel: ReadGoalsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val currentReadFormState by remember { mutableStateOf(readGoalsScreenViewModel.currentReadFormState) }

    val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState
        .collectAsStateWithLifecycle(
            initialValue = ImageSelectorUiState(),
            minActiveState = Lifecycle.State.STARTED
        )


    StatelessReadGoalScreen(
        currentReadFormState = currentReadFormState,
        popBackStack = {
            homeNavigator.openHomeScreen()
        },
        onGoogleIconClicked = {

        },
        imageSelectorState = imageSelectorState.value,
        onSaveClicked = {

            val formValidationResult = currentReadFormState.validate()

            readGoalsScreenViewModel.validateImageSelected(
                imageSelectedUri = imageSelectorState.value.imageSelectedUri
            )

            if (formValidationResult && !imageSelectorState.value.isError) {
                //start saving process to db
                Toast.makeText(
                    context,
                    "Form is properly filled , saving data...",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        },
        onImageSelectorClicked = {

            /*currentSelectedImage = "some image selected"
            imageSelectedError = currentSelectedImage.isBlank()*/

            //1.update selector state to show circular progress
            readGoalsScreenViewModel.passScreenAction(
                ReadGoalsScreenViewModel.AddReadFormUiActions.LaunchImagePicker
            )

            //2.launch image picker
            //this current implementation simulates the image picking action will replace
            //with actual image picker implementation
            coroutineScope.launch {
                delay(5000L)

                readGoalsScreenViewModel.passScreenAction(
                    ReadGoalsScreenViewModel.AddReadFormUiActions
                        .ImageSelected(imageUri = "some image uri")
                )

            }


        }
    )
}

@Composable
@Preview(name = "ReadGoalScreen", showBackground = true)
fun StatelessReadGoalScreen(
    currentReadFormState: FormState<TextFieldState> = FormState(fields = listOf()),
    imageSelectorState: ImageSelectorUiState = ImageSelectorUiState(),
    popBackStack: () -> Unit = {},
    onGoogleIconClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onImageSelectorClicked: () -> Unit = {}
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

        CurrentReadForm(
            currentReadFormState = currentReadFormState,
            onSaveBookClicked = {
                onSaveClicked()
            },
            imageSelectorState = imageSelectorState,
            imageSelectorClicked = {
                onImageSelectorClicked()
            }
        )

        BottomNextPreviousButtons(
            modifier = Modifier.padding(16.dp),
            currentPosition = 0
        )

    }
}

@Composable
@Preview("CurrentReadForm")
fun CurrentReadForm(
    //will take in form state
    currentReadFormState: FormState<TextFieldState> = FormState(fields = listOf()),
    imageSelectorState: ImageSelectorUiState = ImageSelectorUiState(),
    onSaveBookClicked: () -> Unit = {},
    imageSelectorClicked: () -> Unit = {}
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {

        ImageSelectorComponent(
            onSelect = {
                //start the image picker
                imageSelectorClicked()
            },
            imageSelectorState = imageSelectorState
        )

        Spacer(modifier = Modifier.height(24.dp))


        val titleFieldState: TextFieldState = currentReadFormState.getState(name = "title")

        TextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Title",
            text = titleFieldState.value,
            hasError = titleFieldState.hasError,
            onTextChanged = { fieldValue ->
                titleFieldState.change(fieldValue)
            }
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .padding(start = 16.dp)
        )

        val authorFieldState: TextFieldState = currentReadFormState.getState("author")
        TextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Author",
            text = authorFieldState.value,
            hasError = authorFieldState.hasError,
            onTextChanged = { fieldValue ->
                authorFieldState.change(fieldValue)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        val chapterDropDownState: TextFieldState = currentReadFormState.getState("chapters")
        val currentChapterDropDownState: TextFieldState =
            currentReadFormState.getState("current chapter")

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
                modifier = Modifier.width(180.dp) ,
                label = "Chapters",
                selectedText = chapterDropDownState.value,
                dropDownItems = chapterCount.toList(),
                hasError = chapterDropDownState.hasError,
                onListItemSelected = { chapter ->
                    Timber.tag("ReadGoalsScreen").d(chapter)
                    chapterDropDownState.change(chapter)
                    //update chapter count
                } ,
                canUserFill = true
            )

            Spacer(modifier = Modifier.width(30.dp))



            DropDownComponent(
                modifier = Modifier.width(180.dp) ,
                label = "Current chapter",
                selectedText = currentChapterDropDownState.value,
                dropDownItems = chapterCount.toList(),
                hasError = currentChapterDropDownState.hasError,
                onListItemSelected = { chapter ->
                    Timber.tag("ReadGoalsScreen").d(chapter)
                    currentChapterDropDownState.change(chapter)
                    //update current selected chapter
                } ,
                canUserFill = true
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        val chapterTitleState: TextFieldState = currentReadFormState.getState("chapter title")

        TextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            text = chapterTitleState.value,
            label = "Current chapter title",
            hasError = chapterTitleState.hasError,
            onTextChanged = { text ->
                chapterTitleState.change(text)
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                //validate with state show error where necessary
                onSaveBookClicked()

            },
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
    imageSelectorState: ImageSelectorUiState = ImageSelectorUiState(),
    onSelect: () -> Unit = {}
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(158.dp)
            .height(191.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .border(
                width = if (imageSelectorState.isError) 2.dp else 0.dp,
                color = if (imageSelectorState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                //starts the image picking flow
                onSelect()
            }
    ) {

        if (imageSelectorState.imageSelectedUri.isNotBlank()) {
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

        if (imageSelectorState.imageSelectedUri.isBlank() && !imageSelectorState.showProgress) {
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
                    text = if (imageSelectorState.isError) "Add image*" else "Add image",
                    style = BookAppTypography.labelLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = if (imageSelectorState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )
            }
        }

        if (imageSelectorState.showProgress) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview("DropDownComponent")
fun DropDownComponent(
    modifier: Modifier = Modifier,
    selectedText: String = "",
    hasError: Boolean = false,
    label: String = "",
    placeHolderText : String = "" ,
    dropDownItems: List<String> = listOf(),
    onListItemSelected: (String) -> Unit = {},
    canUserFill: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        if (label.isNotBlank()) {
            Text(
                modifier = if(label.length > 15) Modifier.fillMaxWidth() else Modifier ,
                text = if (hasError) "$label*" else label,
                style = BookAppTypography.labelMedium,
                color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = modifier
        ) {

            TextField(
                value = selectedText.ifBlank { placeHolderText },
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
@Preview("BottomButtonsSection", showBackground = true)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(name = "OverallGoalsForm", showBackground = true)
fun OverallGoalsForm(
    modifier: Modifier = Modifier,
    hasError: Boolean = false,
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Overall",
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
        )

        Text(
            text = "In a day , I want to read for at least.",
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.labelMedium ,
            color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {

            OutlinedTextField(
                value = "30 min",
                onValueChange = {
                    /*onTextChanged(it)*/
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(0.dp),
                        //if error is available , change the color of border to error color
                        color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                    )
                    .weight(1f),

                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    errorCursorColor = MaterialTheme.colorScheme.error
                ),
                textStyle = BookAppTypography.bodyMedium,
                shape = RoundedCornerShape(0.dp),
                //adjust error depending if error is available
                isError = hasError,
            )

            Spacer(modifier = Modifier.width(20.dp))

            RoundedBrownButton(
                label = "Set",
                icon = R.drawable.baseline_access_alarm_24,
                onClick = {
                    //launch alarm picker dialog
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val dropDownItems = listOf<String>(
            "Every day" ,
            "Every day except" ,
            "Weekend only" ,
            "Weekdays" ,
            "Select specific days"
        )

        DropDownComponent(
            label = "For how long do you want this goal to run?" ,
            modifier = Modifier.fillMaxWidth() ,
            selectedText = "" ,
            canUserFill = false ,
            dropDownItems = dropDownItems ,
            onListItemSelected = {
                //show items
            } ,
            placeHolderText = "nothing selected"
        )

        Spacer(modifier = Modifier.height(16.dp))

        WeekDaySelectorComponent(
            modifier = Modifier.fillMaxWidth() ,
            onDaySelected = {
                //update selected days list
            }
        )

    }

}


