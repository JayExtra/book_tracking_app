package com.dev.james.my_library.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.my_library.presentation.navigation.MyLibraryScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun MyLibraryScreen(
    myLibraryScreenNavigator: MyLibraryScreenNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            text = "My Library Screen" ,
            style = BookAppTypography.bodyLarge ,
            textAlign = TextAlign.Center
        )

    }
}