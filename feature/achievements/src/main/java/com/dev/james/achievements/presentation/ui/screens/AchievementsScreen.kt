package com.dev.james.achievements.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dev.james.achievements.presentation.navigation.AchievementsScreenNavigator
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun AchievementsScreen(
    achievementsScreenNavigator: AchievementsScreenNavigator
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            text = "Achievements" ,
            style = BookAppTypography.bodyLarge ,
            textAlign = TextAlign.Center
        )

    }
}