package com.dev.james.booktracker.home.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R

@Composable
@Preview(showBackground = false)
fun StreakComponent(){
    Card(
        shape = RoundedCornerShape(10.dp) ,
        elevation = CardDefaults.cardElevation(5.dp) ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ) ,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(6.dp) ,
            horizontalArrangement = Arrangement.SpaceBetween ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StreakCounterComponent()

            BookCounterComponent()

        }
    }
}

@Composable
@Preview
fun StreakCounterComponent(){
    val constraintSet = ConstraintSet {
        val flameImage = createRefFor("flame_item")
        val title = createRefFor("streak_title")
        val daysMessage = createRefFor("days_item")
        val checkIcon = createRefFor("check_item")
        val trackMessage = createRefFor("on_track_item")

        constrain(flameImage){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
        }

        constrain(title){
            top.linkTo(flameImage.top)
            start.linkTo(flameImage.end , 8.dp)
            //end.linkTo(parent.end)
        }

        constrain(daysMessage){
            top.linkTo(title.bottom)
            start.linkTo(title.start)
           // end.linkTo(title.end)
        }

        constrain(checkIcon){
            top.linkTo(daysMessage.bottom)
            start.linkTo(daysMessage.start)
        }
        constrain(trackMessage){
            top.linkTo(checkIcon.top)
            start.linkTo(checkIcon.end , 2.dp)
            bottom.linkTo(checkIcon.bottom)
        }

    }

    ConstraintLayout(
        constraintSet = constraintSet ,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.secondary)
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .layoutId("flame_item"),
            contentAlignment = Alignment.Center
        ){

            Icon(
                painter = painterResource(id = R.drawable.ic_flame_24),
                contentDescription = "" ,
                modifier = Modifier.size(60.dp) ,
                tint = Color.White
            )
        }

        Text(
            text = "streak" ,
            color = MaterialTheme.colorScheme.primary ,
            style = BookAppTypography.labelMedium,
            modifier = Modifier.layoutId("streak_title")
        )

        Text(
            text = "7 days" ,
            color = MaterialTheme.colorScheme.primary ,
            textAlign = TextAlign.Start,
            style = BookAppTypography.bodyLarge,
            fontSize = 24.sp,
            modifier = Modifier.layoutId("days_item")
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_check_24),
            contentDescription = "" ,
            modifier = Modifier
                .size(18.dp)
                .layoutId("check_item") ,
            tint = Color.Green
        )

        Text(
            text = "On track" ,
            color = MaterialTheme.colorScheme.primary ,
            textAlign = TextAlign.Start,
            style = BookAppTypography.bodySmall,
            fontSize = 10.sp ,
            modifier = Modifier.layoutId("on_track_item")
        )
    }
}

@Composable
@Preview
fun BookCounterComponent(){
    val constraintSet = ConstraintSet {
        val flameImage = createRefFor("book_item")
        val title = createRefFor("read_title")
        val daysMessage = createRefFor("book_count_item")
        val checkIcon = createRefFor("book_icon_item")
        val trackMessage = createRefFor("target_track_item")

        constrain(flameImage){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
        }

        constrain(title){
            top.linkTo(flameImage.top)
            start.linkTo(flameImage.end , 8.dp)
            //end.linkTo(parent.end)
        }

        constrain(daysMessage){
            top.linkTo(title.bottom)
            start.linkTo(title.start)
            // end.linkTo(title.end)
        }

        constrain(checkIcon){
            top.linkTo(daysMessage.bottom)
            start.linkTo(daysMessage.start)
        }
        constrain(trackMessage){
            top.linkTo(checkIcon.top)
            start.linkTo(checkIcon.end , 2.dp)
            bottom.linkTo(checkIcon.bottom)
        }

    }

    ConstraintLayout(
        constraintSet = constraintSet ,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .layoutId("book_item"),
            contentAlignment = Alignment.Center
        ){

            var animationTriggered by remember {
                mutableStateOf(false)
            }

            val progressValue by animateFloatAsState(
                targetValue = if(animationTriggered) 0.8f else 0f,
                animationSpec = tween(
                    durationMillis = 1000,
                    delayMillis = 0
                ), label = ""
            )
            LaunchedEffect(key1 = true) {
                animationTriggered = true
            }

            CircularProgressIndicator(
                progress = progressValue ,
                strokeWidth = 4.dp ,
                color = MaterialTheme.colorScheme.primary ,
                modifier = Modifier.size(70.dp)
            )

            Text(
                text = "80%" ,
                style = BookAppTypography.labelSmall ,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Text(
            text = "read" ,
            color = MaterialTheme.colorScheme.primary ,
            style = BookAppTypography.labelMedium,
            modifier = Modifier.layoutId("read_title")
        )

        Text(
            text = "5 books" ,
            color = MaterialTheme.colorScheme.primary ,
            textAlign = TextAlign.Start,
            style = BookAppTypography.bodyLarge,
            fontSize = 24.sp,
            modifier = Modifier.layoutId("book_count_item")
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_book_24),
            contentDescription = "" ,
            modifier = Modifier
                .size(18.dp)
                .layoutId("book_icon_item") ,
            tint = Color.Green
        )

        Text(
            text = "target 8" ,
            color = MaterialTheme.colorScheme.primary ,
            textAlign = TextAlign.Start,
            style = BookAppTypography.bodySmall,
            fontSize = 10.sp ,
            modifier = Modifier.layoutId("target_track_item")
        )
    }
}
