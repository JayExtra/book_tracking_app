package com.dev.james.achievements.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.achievements.presentation.navigation.AchievementsScreenNavigator
import com.dev.james.achievements.presentation.ui.viewmodel.AchievementsScreenUIState
import com.dev.james.achievements.presentation.ui.viewmodel.AchievementsViewModel
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.compose_ui.ui.theme.SurfaceDark60
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.core.common_models.UserDetails
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun AchievementsScreen(
    navigator: AchievementsScreenNavigator ,
    viewModel : AchievementsViewModel = hiltViewModel(),
){
    val state = viewModel.state.collectAsStateWithLifecycle()
    AchievementsStatelessScreen(
        state = state.value ,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AchievementsStatelessScreen(
    state : AchievementsScreenUIState = AchievementsScreenUIState(),
    onAction : (AchievementsViewModel.AchievementsScreenUIActions) -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UserDetailsCard()
        ReadingOverviewSection()
    }

}

@Composable
private fun HistorySection(){
    //data to show
    // 1. Goal progress
    // 2. Popular genre
    // 3. Popular authors
    // 4. least/most engaged books
    // 5. reading frequency: day/weekend warrior/night owl

}

@Preview(showBackground = true)
@Composable
private fun ReadingOverviewSection(){
    //data to show
    // last 30 days & all time:
    // 1. sessions started
    // 2. total time
    // 3. pages read
    // 4. books completed
    // 5. avarage time spent per session

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() ,
        verticalArrangement = Arrangement.Top ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Reading Overview" ,
            textAlign = TextAlign.Center ,
            style = BookAppTypography.headlineMedium
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Summary of your reading activity." ,
            textAlign = TextAlign.Center ,
            style = BookAppTypography.bodyMedium,
            color = SurfaceDark60
        )
        Spacer(modifier = Modifier.height(14.dp))

        LazyVerticalGrid(
            modifier = Modifier
                .height(400.dp)
                .padding(start = 30.dp, end = 30.dp, top = 8.dp),
            columns = GridCells.Fixed(2) ,
            verticalArrangement = Arrangement.spacedBy(15.dp) ,
            horizontalArrangement = Arrangement.Center ,
            userScrollEnabled = false , 

        ){
            repeat(8){
                item {
                    StatisticPlaque()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticPlaque(
    modifier : Modifier = Modifier ,
    data : String = "10" ,
    metric : String = "books" ,
    period : String = "ALL TIME"
){
    val metricText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Bold , fontSize = 20.sp)
        ){
            append(data)
        }
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Bold , fontSize = 14.sp)
        ){
            append(" $metric")
        }
    }
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight() ,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = metricText , style = BookAppTypography.bodyMedium )
        Text(
            text = period ,
            style = BookAppTypography.bodyMedium ,
            fontWeight = FontWeight.Bold ,
            fontSize = 12.sp ,
            color = SurfaceDark60
        )
    }
}

@Preview
@Composable
private fun UserDetailsCard(
    user : UserDetails = UserDetails()
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() ,
        shape = RoundedCornerShape(corner = CornerSize(10.dp))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(6.dp)
        ){
            //TODO:Image maybe avatar or image url subjected to change
            CoilImageComponent(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(60.dp),
                image = ""
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = user.username ,
                style = BookAppTypography.bodyMedium ,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun AchievementsScreenLightPreview(){
    BookTrackerTheme {
        AchievementsStatelessScreen(
            state = AchievementsScreenUIState() ,
            onAction = {}        
        )
    }
}

@Preview
@Composable
private fun AchievementsScreenDarkPreview(){
    BookTrackerTheme(
        theme = Theme.DARK_THEME.themeValue
    ) {
        AchievementsStatelessScreen(
            state = AchievementsScreenUIState() ,
            onAction = {}
        )
    }
}