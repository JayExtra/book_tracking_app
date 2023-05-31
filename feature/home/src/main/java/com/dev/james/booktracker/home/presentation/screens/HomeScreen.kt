package com.dev.james.booktracker.home.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppShapes
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
@Preview("Home Screen")
fun HomeScreen() {

    Box(
     modifier = Modifier.fillMaxSize() ,
     contentAlignment = Alignment.TopCenter
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center ,
                modifier = Modifier.padding(16.dp) ,
                color = Color.White
            )
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = BookAppShapes.medium ,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onClick = {
            /*navigate or show goal addition bottom sheet*/
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add action" ,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

}
