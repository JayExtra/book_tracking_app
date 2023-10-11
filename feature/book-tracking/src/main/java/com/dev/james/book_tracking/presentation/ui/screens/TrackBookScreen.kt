

package com.dev.james.book_tracking.presentation.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.dev.james.book_tracking.R
import com.dev.james.domain.utilities.StopWatch
import com.dev.james.book_tracking.presentation.ui.navigation.BookTrackNavigation
import com.dev.james.book_tracking.presentation.viewmodel.BookTrackingViewModel
import com.dev.james.booktracker.compose_ui.ui.components.BarGraph
import com.dev.james.booktracker.compose_ui.ui.components.OutlinedTextFieldComponent
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.BookStatsData
import com.ramcosta.composedestinations.annotation.Destination

@RequiresApi(Build.VERSION_CODES.O)
@Composable
/*@Preview(
    showBackground = true,
    widthDp = 405 ,
    heightDp = 1060
)*/
@Destination
fun TrackBookScreen(
    navigation : BookTrackNavigation ,
    bookTrackingViewModel: BookTrackingViewModel = hiltViewModel(),
    bookId : String?
) {

    LaunchedEffect(key1 = true){
        bookId?.let {
            bookTrackingViewModel.getBookStatistics()
        }
    }

    val stopWatch = remember {
        StopWatch()
    }

    var isStopWatchRunning by remember {
        mutableStateOf(false)
    }

    val bookData = bookTrackingViewModel.bookStatsState.collectAsStateWithLifecycle()


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

            var showTimerAndTrackCard by remember {
                mutableStateOf(false)
            }

            BookProgressSection(
                showTimerText = showTimerAndTrackCard,
                bookData = bookData.value,
                timerValue = stopWatch.formattedTime,
                timerRunning = isStopWatchRunning,
                onShowTimerText = { show ->
                    showTimerAndTrackCard = show
                    if(isStopWatchRunning){
                        isStopWatchRunning = false
                        stopWatch.pause()
                    }else{
                        isStopWatchRunning = true
                        stopWatch.start()
                    }
                }
            )

            ProgressGraphSection()

            AnimatedVisibility(
                visible = showTimerAndTrackCard ,
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
                ) + slideOutVertically(
                    animationSpec = tween(durationMillis = 500),
                    targetOffsetY = { -it / 2 }
                ),
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
                ) + slideInVertically(
                    animationSpec = tween(durationMillis = 500),
                    initialOffsetY = { -it / 2 }
                )
            ){
                var chapterFieldErrorState by remember {
                    mutableStateOf(false)
                }
                TrackSection(
                    canFinishSession = isStopWatchRunning,
                    errorInChapterField = chapterFieldErrorState,
                    onFinishSession = { chapterTitle , chapterCount ->
                        showTimerAndTrackCard = false
                        isStopWatchRunning = false
                        if(chapterTitle.isEmpty()){
                            chapterFieldErrorState = true
                        }else {
                            chapterFieldErrorState = false
                            stopWatch.reset()
                        }

                    }
                )
            }
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
            HoursWithEmojiComponent(
                modifier = Modifier.size(90.dp)
            )
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
fun BookProgressSection(
    bookData: BookProgressData = BookProgressData() ,
    timerValue : String = "00:00:00" ,
    showTimerText : Boolean = false ,
    timerRunning : Boolean = false ,
    onShowTimerText : (Boolean) -> Unit = {}
){
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
                top.linkTo(chapterSet.bottom , 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
        }


        constrain(startBtnSet){
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            if(showTimerText) top.linkTo(timerSet.bottom , 8.dp) else top.linkTo(chapterSet.bottom , 8.dp)
        }
    }

    ConstraintLayout(
        constraintSet = constraints ,
        modifier = Modifier.fillMaxWidth()
    ) {

        BookProgressImageSection(
            modifier = Modifier.layoutId("progress_image") ,
            bookData = bookData
        )

        Text(
            text = bookData.bookTitle ,
            style = BookAppTypography.labelLarge ,
            modifier = Modifier.layoutId("book_title") ,
            fontSize = 20.sp
            )
        Text(
            text = bookData.authors ,
            style = BookAppTypography.bodyMedium ,
            modifier = Modifier.layoutId("author_title") ,
            fontSize = 15.sp
        )

        Row(modifier = Modifier.layoutId("chapter_title")) {

            Icon(painter = painterResource(id = R.drawable.ic_bookmark_24), contentDescription ="" )
            Text(
                text = "chapter ${bookData.currentChapter}: ${bookData.currentChapterTitle}" ,
                style = BookAppTypography.bodySmall ,
                fontSize = 13.sp ,
                modifier = Modifier.padding(top = 2.dp , start = 2.dp)
                )
        }

        AnimatedVisibility(
            visible = showTimerText ,
            exit = fadeOut(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + slideOutVertically(
                animationSpec = tween(durationMillis = 200),
                targetOffsetY = { -it / 2 }
            ),
            enter = fadeIn(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + slideInVertically(
                animationSpec = tween(durationMillis = 200),
                initialOffsetY = { -it / 2 }
            ) ,
            modifier = Modifier
                .layoutId("timer")
                .fillMaxWidth()
        )
        {
            Text(
                text = timerValue,
                style = BookAppTypography.headlineMedium ,
                fontSize = 36.sp ,
                textAlign = TextAlign.Center ,
                modifier = Modifier.fillMaxWidth()
            )
        }


        ElevatedButton(
            modifier = Modifier.layoutId("start_button"),
            onClick = {
            //will start timer
                onShowTimerText(true)
             } ,
            shape = RoundedCornerShape(10.dp) ,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            val buttonText = if(timerRunning) "pause" else "start"
            Text(text = buttonText , style = BookAppTypography.labelLarge)
        }

    }
}

@Composable
@Preview(showBackground = true)
fun TrackSection(
    canFinishSession : Boolean = false,
    errorInChapterField : Boolean = false,
    onFinishSession : (String , Int) -> Unit = { _ , _  ->  }
){
    var chapterTitle by remember {
        mutableStateOf("")
    }
    var finalChapterCount by remember {
        mutableStateOf(0)
    }
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ) ,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp) ,
        shape = RoundedCornerShape(10.dp)
    ) {

        Column(modifier = Modifier.padding(8.dp) , verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(modifier = Modifier.fillMaxWidth() , text = "Track" , style = BookAppTypography.headlineSmall)
           // CounterSection(title = "Page")

            CounterSection(
                title = "Chapter" ,
                chapterCount = finalChapterCount ,
                increaseValue = {
                    finalChapterCount += 1
                } ,
                decreaseValue = {
                    if(finalChapterCount > 0){
                        finalChapterCount -= 1
                    }

                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            AnimatedVisibility(
                visible = !canFinishSession ,
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
                ),
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
                ) ,
                modifier = Modifier
                    .layoutId("timer")
                    .fillMaxWidth()
            ){

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextFieldComponent(
                        modifier = Modifier.fillMaxWidth() ,
                        label = "Please indicate your current chapter",
                        text = chapterTitle ,
                        onTextChanged = {
                            chapterTitle = it
                        } ,
                        hasError = errorInChapterField ,
                        isSingleLine = true
                    )

                    ElevatedButton(
                        onClick = {
                            onFinishSession(chapterTitle , finalChapterCount)
                        } , colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ) ,
                        modifier = Modifier.fillMaxWidth() ,
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text("finish session" , style = BookAppTypography.labelMedium)
                    }
                }

            }

        }
    }
}

@Composable
@Preview(showBackground = true)
fun CounterSection(
    title : String = "Title" ,
    chapterCount : Int = 0,
    increaseValue: () -> Unit = {} ,
    decreaseValue: () -> Unit = {}
){
   Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween , verticalAlignment = Alignment.CenterVertically) {
    Text(text = title , style = BookAppTypography.labelMedium)
    CounterButtonsComponent(
        countValue = chapterCount ,
        increaseValue =  {
            increaseValue()
        } ,
        decreaseValue = {
            decreaseValue()
        }
    )
   }
}

@Composable
@Preview(showBackground = true)
fun CounterButtonsComponent(
    countValue : Int = 0 ,
    increaseValue : () -> Unit = {} ,
    decreaseValue : () -> Unit = {}
){
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
            onClick = {
                decreaseValue()
            } ,
            shape = RoundedCornerShape(10.dp) ,
            contentPadding = PaddingValues(2.dp)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "" , modifier = Modifier.size(35.dp) )
        }
        Text(modifier = Modifier
            .layoutId("count_text")
            .padding(start = 6.dp, end = 6.dp), text = countValue.toString() , style = BookAppTypography.labelMedium , fontSize = 24.sp)

        OutlinedButton(
            modifier = Modifier.layoutId("increase_text"),
            onClick = {
                      increaseValue()
            } ,
            shape = RoundedCornerShape(10.dp) ,
            contentPadding = PaddingValues(2.dp)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "" , modifier = Modifier.size(35.dp) )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HoursWithEmojiComponent(modifier : Modifier = Modifier){
    Column(verticalArrangement = Arrangement.spacedBy(5.dp) , modifier = modifier) {
        Text(text = "This week" , style = BookAppTypography.bodyMedium , modifier = Modifier.fillMaxWidth() , textAlign = TextAlign.Center , fontSize = 14.sp)
        Text(text = "25 hours" , style = BookAppTypography.labelLarge , modifier = Modifier.fillMaxWidth() , textAlign = TextAlign.Center , fontSize = 20.sp , color = MaterialTheme.colorScheme.secondary)
        Box(contentAlignment = Alignment.Center , modifier = Modifier
            .width(70.dp)
            .height(70.dp)) {
            Image(painter = painterResource(id = R.drawable.happy_emoji), contentDescription ="", modifier = Modifier.size(60.dp) )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun BookProgressImageSection(
    modifier: Modifier = Modifier ,
    bookData : BookProgressData = BookProgressData(),
) {
    var animationTriggered by remember{
        mutableStateOf(false)
    }
    LaunchedEffect(true) {
        animationTriggered = true
    }
    val finalProgress = animateFloatAsState(
        targetValue = if (animationTriggered) bookData.progress else 0f,
        label = "book_final_progress" ,
        animationSpec = tween(delayMillis = 500)
    )



    Box(
        contentAlignment = Alignment.Center ,
        modifier = modifier.padding(8.dp)
    ) {

        val coilImage = rememberAsyncImagePainter(
            ImageRequest
                .Builder(LocalContext.current)
                .data(data = bookData.bookImage)
                .apply(block = {
                    //placeholder(R.drawable.image_placeholder_24)
                    error(R.drawable.ic_error_24)
                    scale(Scale.FIT)
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
                .size(width = 75.dp, height = 110.dp) ,
               // .clip(RoundedCornerShape(5.dp)) ,
            contentScale = ContentScale.Crop
        )

        CircularProgressIndicator(
            progress = finalProgress.value ,
            strokeCap = StrokeCap.Round,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(width = 161.dp, height = 170.dp),
            strokeWidth = 12.dp
        )
    }
}
