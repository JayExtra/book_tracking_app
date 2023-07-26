package com.dev.james.booktracker.compose_ui.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.*
import com.dev.james.booktracker.compose_ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedInputText(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    @DrawableRes icon: Int,
    text: String,
    isErrorEnabled : Boolean = false,
){

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(10.dp)),
        value = text ,
        onValueChange = {
            onValueChanged(it)
        } ,
        textStyle = MaterialTheme.typography.bodyMedium ,
        leadingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "User icon" ,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        } ,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ) ,
        singleLine = true ,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = GrayHue ,
            focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary ,
            cursorColor = MaterialTheme.colorScheme.onPrimary ,
            errorIndicatorColor = ErrorColor49 ,
            errorLabelColor = ErrorColor49 ,
            errorCursorColor = ErrorColor49 ,
            containerColor = MaterialTheme.colorScheme.onBackground ,
           // textColor = MaterialTheme.colorScheme.onPrimary
        ) ,
        isError = isErrorEnabled
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun RoundedInputTextPreview(
    modifier: Modifier = Modifier ,
){
    var text = "Some text"
    TextField(
        modifier = modifier
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(10.dp)),
        value = text ,
        onValueChange = {
             text = it
        } ,
        textStyle = MaterialTheme.typography.bodyMedium ,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.outline_account_circle_24),
                contentDescription = "User icon" ,
                tint = Brown50
            )
        } ,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ) ,
        singleLine = true ,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = GrayHue ,
            focusedIndicatorColor = GrayHue ,
            cursorColor = Orange40 ,
            errorIndicatorColor = ErrorColor49 ,
            errorLabelColor = ErrorColor49 ,
            errorCursorColor = ErrorColor49 ,
        ) ,
    )
}
