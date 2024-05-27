package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.compose_ui.ui.theme.SurfaceDark40
import com.dev.james.booktracker.compose_ui.ui.theme.SurfaceDark80
import com.dev.james.booktracker.compose_ui.ui.theme.Theme

@Preview
@Composable
fun SearchTextFieldComponent(
    modifier : Modifier = Modifier ,
    text : String = "" ,
    onTextChanged : (String) -> Unit = {} ,
    onClearText : () -> Unit = {}
){

        TextField(
            modifier = modifier,
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            } ,
            trailingIcon = {
                if(text.isNotEmpty()){
                    Icon(modifier = Modifier.clickable( onClick = { onClearText() }) ,imageVector = Icons.Default.Close, contentDescription = "")
                }
            },
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent ,
                /*focusedContainerColor = SurfaceDark80 ,
                unfocusedContainerColor = SurfaceDark80,
                focusedLeadingIconColor = SurfaceDark40,
                unfocusedLeadingIconColor = SurfaceDark40,
                focusedPlaceholderColor = SurfaceDark40 ,
                unfocusedLabelColor = SurfaceDark40 ,
                focusedTextColor = SurfaceDark40 ,
                unfocusedTextColor = SurfaceDark40*/
            ) ,
            placeholder = {
                Text(
                   text = "Input the book title",
                    style = BookAppTypography.bodySmall,
                    color = SurfaceDark40
                )
            }
        )
}


@Preview(showBackground = true)
@Composable
private fun SearchTextFieldDarkPreview(){
    BookTrackerTheme(theme = Theme.DARK_THEME.themeValue) {
        SearchTextFieldComponent()
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldLightPreview(){
    BookTrackerTheme(theme = Theme.LIGHT_THEME.themeValue) {
        SearchTextFieldComponent()
    }
}