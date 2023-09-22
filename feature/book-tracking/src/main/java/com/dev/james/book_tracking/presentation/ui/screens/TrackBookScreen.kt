

package com.dev.james.book_tracking.presentation.ui.screens

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.dev.james.book_tracking.R
import com.dev.james.book_tracking.presentation.ui.navigation.BookTrackNavigation
import com.dev.james.booktracker.compose_ui.ui.components.BarGraph
import com.dev.james.booktracker.compose_ui.ui.components.OutlinedTextFieldComponent
import com.dev.james.booktracker.compose_ui.ui.components.RoundedInputText
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.ramcosta.composedestinations.annotation.Destination

@Composable
/*@Preview(
    showBackground = true,
    widthDp = 405 ,
    heightDp = 1060
)*/
@Destination
fun TrackBookScreen(
    navigation : BookTrackNavigation
) {
    Column(modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.spacedBy(8.dp)) {
        StandardToolBar(
            showBackArrow = true ,
            title = {
                Text(text = "Progress", style = BookAppTypography.headlineSmall)
            } ,
            navigate = {
                navigation.backToHomeScreen()
            }
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp) , modifier = Modifier.verticalScroll(
            state =  rememberScrollState())
        ) {
            BookProgressSection()
            ProgressGraphSection()
            TrackSection()
        }
    }

}

@Composable
@Preview(showBackground = true)
fun ProgressGraphSection(){
    Card(
        shape = RoundedCornerShape(10.dp) ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) ,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Text("History" , style = BookAppTypography.headlineSmall, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            HoursWithEmojiComponent()
            BarGraph(
                height = 150.dp ,
                graphBarData = mapOf("Sun" to 7200000L , "Mon" to 3600000L , "Teu" to 1800000L , "Wen" to 1200000L , "Thur" to 3600000L , "Fri" to 2400000L , "Sat" to 600000L
                )
            )

        }
    }

}

@Composable
@Preview(showBackground = true)
fun BookProgressSection(){
    val constraints = ConstraintSet {
        val imageSet = createRefFor("progress_image")
        val titleSet = createRefFor("book_title")
        val authorSet = createRefFor("author_title")
        val chapterSet = createRefFor("chapter_title")
        val timerSet = createRefFor("timer")
        val startBtnSet = createRefFor("start_button")

        constrain(imageSet){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(titleSet){
            top.linkTo(imageSet.bottom)
            start.linkTo(imageSet.start)
            end.linkTo(imageSet.end)
        }
        constrain(authorSet){
            top.linkTo(titleSet.bottom)
            start.linkTo(titleSet.start)
            end.linkTo(titleSet.end)
        }
        constrain(chapterSet){
            top.linkTo(authorSet.bottom)
            start.linkTo(authorSet.start)
            end.linkTo(authorSet.end)
        }
        constrain(timerSet){
            top.linkTo(chapterSet.bottom , 16.dp)
            start.linkTo(chapterSet.start)
            end.linkTo(chapterSet.end)
        }
        constrain(startBtnSet){
            top.linkTo(timerSet.bottom , 6.dp)
            start.linkTo(timerSet.start)
            end.linkTo(timerSet.end)
        }
    }

    ConstraintLayout(
        constraintSet = constraints ,
        modifier = Modifier.fillMaxWidth()
    ) {

        BookProgressImageSection(
            modifier = Modifier.layoutId("progress_image")
        )

        Text(
            text = "Some title" ,
            style = BookAppTypography.labelLarge ,
            modifier = Modifier.layoutId("book_title") ,
            fontSize = 20.sp
            )
        Text(
            text = "Some author" ,
            style = BookAppTypography.bodyMedium ,
            modifier = Modifier.layoutId("author_title") ,
            fontSize = 15.sp
        )

        Row(modifier = Modifier.layoutId("chapter_title")) {

            Icon(painter = painterResource(id = R.drawable.ic_bookmark_24), contentDescription ="" )
            Text(
                text = "chapter 4: Some chapter title" ,
                style = BookAppTypography.bodySmall ,
                fontSize = 13.sp ,
                modifier = Modifier.padding(top = 2.dp , start = 2.dp)
                )
        }

        Text(
            text = "00:00:00" ,
            style = BookAppTypography.headlineMedium ,
            modifier = Modifier.layoutId("timer") ,
            fontSize = 36.sp
            )

        ElevatedButton(
            modifier = Modifier.layoutId("start_button"),
            onClick = {
            //will start timer
             } ,
            shape = RoundedCornerShape(10.dp) ,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = "start" , style = BookAppTypography.labelLarge)
        }

    }
}

@Composable
@Preview(showBackground = true)
fun TrackSection(){
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ) ,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp) ,
        shape = RoundedCornerShape(10.dp)
    ) {
        var text by remember {
            mutableStateOf("")
        }
        Column(modifier = Modifier.padding(8.dp) , verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(modifier = Modifier.fillMaxWidth() , text = "Track" , style = BookAppTypography.headlineSmall)
            CounterSection(title = "Page")
            CounterSection(title = "Chapter")

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextFieldComponent(
                modifier = Modifier.fillMaxWidth() ,
                text = text ,
                onTextChanged = {
                    text = it
                }
            )

            ElevatedButton(onClick = {  } , colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ) ,
                modifier = Modifier.fillMaxWidth() ,
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("finish session" , style = BookAppTypography.labelSmall)
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
fun CounterSection(title : String = "Title"){
   Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.CenterVertically) {
    Text(text = title , style = BookAppTypography.labelMedium)
    CounterButtonsComponent()
   }
}

@Composable
@Preview(showBackground = true)
fun CounterButtonsComponent(){
    val constraintSet = ConstraintSet {

        val decreaseBtn = createRefFor("decrease_btn")
        val countText = createRefFor("count_text")
        val increaseBtn = createRefFor("increase_text")

        constrain(increaseBtn){
            end.linkTo(parent.end , 4.dp)
            top.linkTo(countText.top)
            bottom.linkTo(countText.bottom)
        }
        constrain(countText){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
        constrain(decreaseBtn){
            top.linkTo(countText.top)
            bottom.linkTo(countText.bottom)
            start.linkTo(parent.start , 4.dp)
        }
        createHorizontalChain(decreaseBtn , countText , increaseBtn , chainStyle = ChainStyle.Spread)
    }
    ConstraintLayout(constraintSet) {
        OutlinedButton(
            modifier = Modifier.layoutId("decrease_btn"),
            onClick = { } ,
            shape = RoundedCornerShape(10.dp) ,
            contentPadding = PaddingValues(2.dp)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "" , modifier = Modifier.size(35.dp) )
        }
        Text(modifier = Modifier
            .layoutId("count_text")
            .padding(start = 6.dp, end = 6.dp), text = "0" , style = BookAppTypography.labelMedium , fontSize = 24.sp)

        OutlinedButton(
            modifier = Modifier.layoutId("increase_text"),
            onClick = { } ,
            shape = RoundedCornerShape(10.dp) ,
            contentPadding = PaddingValues(2.dp)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "" , modifier = Modifier.size(35.dp) )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HoursWithEmojiComponent(){
    Column(verticalArrangement = Arrangement.spacedBy(5.dp) , modifier = Modifier.width(100.dp)) {
        Text(text = "This week" , style = BookAppTypography.bodyMedium , modifier = Modifier.fillMaxWidth() , textAlign = TextAlign.Center , fontSize = 14.sp)
        Text(text = "25 hours" , style = BookAppTypography.labelLarge , modifier = Modifier.fillMaxWidth() , textAlign = TextAlign.Center , fontSize = 20.sp , color = MaterialTheme.colorScheme.secondary)
        Box(contentAlignment = Alignment.Center , modifier = Modifier.size(100.dp)) {
            Image(painter = painterResource(id = R.drawable.happy_emoji), contentDescription ="", Modifier.size(70.dp) )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun BookProgressImageSection(
    modifier: Modifier = Modifier ,
    image : String = "" ,
) {
    Box(
        contentAlignment = Alignment.Center ,
        modifier = modifier.padding(8.dp)
    ) {

        val coilImage = rememberAsyncImagePainter(
            ImageRequest
                .Builder(LocalContext.current)
                .data(data = image)
                .apply(block = {
                    //placeholder(R.drawable.image_placeholder_24)
                    error(R.drawable.ic_error_24)
                    crossfade(true)
                    transformations(
                        RoundedCornersTransformation(0f)
                    )
                }).build()
        )

     //   val painterState = coilImage.state
        //glide image
        Image(
            painter =  coilImage ,
            contentDescription = "",
            modifier = Modifier
                .size(width = 65.dp, height = 100.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        CircularProgressIndicator(
            progress = 0.8f,
            strokeCap = StrokeCap.Round,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(width = 161.dp, height = 170.dp),
            strokeWidth = 12.dp
        )
    }
}
