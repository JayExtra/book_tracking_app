package com.dev.james.booktracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppShapes
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@Composable
@Preview
fun MainTopBar(
    modifier: Modifier = Modifier ,
    avatarImage : Int = 0 ,
    username : String = "Test username" ,
    onSettingsClick : () -> Unit = {} ,
){

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly ,
        verticalAlignment = Alignment.CenterVertically ,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .fillMaxWidth()
    ) {
        Text(
            text = username ,
            style = BookAppTypography.bodyMedium ,
        )
        
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
            Image(
                painter = painterResource(id = avatarImage),
                contentDescription = "avatar image" ,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}