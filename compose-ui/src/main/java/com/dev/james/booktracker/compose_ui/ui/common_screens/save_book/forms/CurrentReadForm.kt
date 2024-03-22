package com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.ImageSelectorUiState
import com.dev.james.booktracker.compose_ui.ui.components.DropDownComponent
import com.dev.james.booktracker.compose_ui.ui.components.ImageSelectorComponent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.components.OutlinedTextFieldComponent
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import timber.log.Timber

@Composable
@Preview("CurrentReadForm")
fun CurrentReadForm(
    //will take in form state
    modifier: Modifier = Modifier,
    currentReadFormState: FormState<TextFieldState> = FormState(fields = listOf()),
    imageSelectorState: ImageSelectorUiState = ImageSelectorUiState(),
    onSaveBookClicked: () -> Unit = {},
    imageSelectorClicked: () -> Unit = {},
    onClearImage : () -> Unit = {}
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        ImageSelectorComponent(
            height = 191.dp,
            width = 140.dp,
           // isCircular = false
            onSelect = {
                //start the image picker
                imageSelectorClicked()
            },
            imageSelectorState = imageSelectorState ,
            onClear = {
                onClearImage()
            }
        )

        Spacer(modifier = Modifier.height(24.dp))


        val titleFieldState: TextFieldState = currentReadFormState.getState(name = "title")

        OutlinedTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Title",
            text = titleFieldState.value,
            hasError = titleFieldState.hasError,
            onTextChanged = { fieldValue ->
                titleFieldState.change(fieldValue)
            } ,
            trailingIcon = Icons.Default.Close,
            onTrailingIconClicked = {
                 titleFieldState.change("")
            },
            isSingleLine = true
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .padding(start = 16.dp)
        )

        val authorFieldState: TextFieldState = currentReadFormState.getState("author")
        OutlinedTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Author",
            text = authorFieldState.value,
            hasError = authorFieldState.hasError,
            onTextChanged = { fieldValue ->
                authorFieldState.change(fieldValue)
            } ,
            trailingIcon = Icons.Default.Close,
            onTrailingIconClicked = {
                //clear the text field
                authorFieldState.change("")
            },
            isSingleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        val descriptionFieldState : TextFieldState = currentReadFormState.getState("description")

        OutlinedTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Description",
            text = descriptionFieldState.value,
            hasError = descriptionFieldState.hasError,
            onTextChanged = { fieldValue ->
                descriptionFieldState.change(fieldValue)
            } ,
            trailingIcon = Icons.Default.Close,
            onTrailingIconClicked = {
                //clear the text field
                descriptionFieldState.change("")
            },
            isSingleLine = false ,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        val categoryFieldState : TextFieldState = currentReadFormState.getState("category")
        val categoriesList = listOf(
            "biography & autobiography" ,
            "comics & graphic novels" ,
            "business & economics" ,
            "drama" ,
            "thriller",
            "true crime" ,
            "fiction",
            "travel" ,
            "nature" ,
            "music" ,
            "mathematics" ,
            "pets" ,
            "philosophy",
            "self-help" ,
            "technology & engineering",
            "computers" ,
            "young adult fiction" ,
            "young adult non-fiction",
            "literary criticism" ,
            "body, mind & spirit"
        )

        DropDownComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "category",
            selectedText = categoryFieldState.value,
            dropDownItems = categoriesList ,
            hasError = categoryFieldState.hasError,
            onListItemSelected = { chapter ->
                Timber.tag("ReadGoalsScreen").d(chapter)
                categoryFieldState.change(chapter)
                //update chapter count
            },
            canUserFill = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        val pagesTextFieldState : TextFieldState = currentReadFormState.getState("pages_count")
        OutlinedTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Pages count",
            text = pagesTextFieldState.value,
            hasError = pagesTextFieldState.hasError,
            onTextChanged = { fieldValue ->
                pagesTextFieldState.change(fieldValue)
            } ,
            trailingIcon = Icons.Default.Close,
            onTrailingIconClicked = {
                //clear the text field
                pagesTextFieldState.change("")
            },
            isSingleLine = true ,
            keyBoardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(16.dp))


        val chapterFieldState: TextFieldState = currentReadFormState.getState("chapters")

        OutlinedTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            label = "Chapters count",
            text = chapterFieldState.value,
            hasError = chapterFieldState.hasError,
            onTextChanged = { fieldValue ->
                chapterFieldState.change(fieldValue)
            } ,
            trailingIcon = Icons.Default.Close,
            onTrailingIconClicked = {
                //clear the text field
                chapterFieldState.change("")
            },
            isSingleLine = true ,
            keyBoardType = KeyboardType.Number
        )

        /*       Row(
                   horizontalArrangement = Arrangement.SpaceEvenly,
                   verticalAlignment = Alignment.CenterVertically ,
                   modifier = Modifier.fillMaxWidth()
               ) {



                   Spacer(modifier = Modifier.width(20.dp))

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

               OutlinedTextFieldComponent(
                   modifier = Modifier.fillMaxWidth(),
                   text = chapterTitleState.value,
                   label = "Current chapter title",
                   hasError = chapterTitleState.hasError,
                   onTextChanged = { text ->
                       chapterTitleState.change(text)
                   } ,
                   trailingIcon = Icons.Default.Close ,
                   onTrailingIconClicked = {
                       chapterTitleState.change("")
                   }  ,
                   isSingleLine = true
               )
       */
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

