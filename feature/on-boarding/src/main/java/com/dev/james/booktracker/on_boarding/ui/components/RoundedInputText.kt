package com.dev.james.booktracker.on_boarding.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.*
import com.dev.james.on_boarding.R

@Composable
fun RoundedInputText(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    @DrawableRes icon: Int,
    text: String
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
        textStyle = MaterialTheme.typography.body1 ,
        leadingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "User icon" ,
                tint = BrownLight
            )
        } ,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ) ,
        singleLine = true ,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = GrayHue ,
            focusedIndicatorColor = GrayHue ,
            cursorColor = Orange ,
            errorIndicatorColor = ErrorColor ,
            errorLabelColor = ErrorColor ,
            errorCursorColor = ErrorColor
        ) ,
    )

}

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
        textStyle = MaterialTheme.typography.body1 ,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.outline_account_circle_24),
                contentDescription = "User icon" ,
                tint = BrownLight
            )
        } ,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ) ,
        singleLine = true ,
        colors = TextFieldDefaults.textFieldColors(
            unfocusedIndicatorColor = GrayHue ,
            focusedIndicatorColor = GrayHue ,
            cursorColor = Orange ,
            errorIndicatorColor = ErrorColor ,
            errorLabelColor = ErrorColor ,
            errorCursorColor = ErrorColor ,
        ) ,
    )
}
