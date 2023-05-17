package com.dev.james.booktracker.home.presentation.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.presentation.components.DropDownComponent
import com.dev.james.booktracker.home.presentation.components.ImageSelectorComponent
import com.dev.james.booktracker.home.presentation.components.TextFieldComponent
import com.dev.james.booktracker.home.presentation.viewmodels.ImageSelectorUiState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import timber.log.Timber

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
                modifier = Modifier.width(180.dp),
                label = "Chapters",
                selectedText = chapterDropDownState.value,
                dropDownItems = chapterCount.toList(),
                hasError = chapterDropDownState.hasError,
                onListItemSelected = { chapter ->
                    Timber.tag("ReadGoalsScreen").d(chapter)
                    chapterDropDownState.change(chapter)
                    //update chapter count
                },
                canUserFill = true
            )

            Spacer(modifier = Modifier.width(30.dp))



            DropDownComponent(
                modifier = Modifier.width(180.dp),
                label = "Current chapter",
                selectedText = currentChapterDropDownState.value,
                dropDownItems = chapterCount.toList(),
                hasError = currentChapterDropDownState.hasError,
                onListItemSelected = { chapter ->
                    Timber.tag("ReadGoalsScreen").d(chapter)
                    currentChapterDropDownState.change(chapter)
                    //update current selected chapter
                },
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

        Spacer(modifier = Modifier.height(12.dp))

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
                style = BookAppTypography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }

}

