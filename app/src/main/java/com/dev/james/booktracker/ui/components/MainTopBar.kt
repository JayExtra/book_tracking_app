package com.dev.james.booktracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppShapes
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@Composable
@Preview
fun MainTopBar(
    modifier: Modifier = Modifier ,
    avatarImage : Int = 0 ,
    username : String = "Test username" ,
    greeting : String = "",
    onSettingsClick : () -> Unit = {} ,
){

    Row(
        horizontalArrangement = Arrangement.Start ,
        verticalAlignment = Alignment.CenterVertically ,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(top = 8.dp , bottom = 8.dp , end = 16.dp , start = 16.dp)
            .fillMaxWidth()
    ) {
        val newText = buildAnnotatedString {
            withStyle(
                style = SpanStyle(fontWeight = FontWeight.Normal)
            ){
                append("Hi $username")
            }
            append("\n")
            withStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold , fontSize = 22.sp)
            ){
                append(greeting)
            }
        }
        Text(
            modifier = Modifier.weight(0.8f) ,
            text = newText,
            style = BookAppTypography.headlineSmall ,
            color = MaterialTheme.colorScheme.secondary
        )

        IconButton(onClick = { onSettingsClick()}) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "settings icon" ,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = BookAppShapes.medium
                )
                .width(50.dp)
                .height(50.dp)
                .clip(shape = BookAppShapes.medium) ,
            contentAlignment = Alignment.Center
        ){
            /* Will replace with coil when image uplaoding becomes possible */
            /*Image(
                painter = painterResource(id = avatarImage),
                contentDescription = "avatar image" ,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )*/
        }
    }

}