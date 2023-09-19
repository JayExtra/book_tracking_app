

package com.dev.james.book_tracking.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.dev.james.book_tracking.R
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@Composable
@Preview(
    showBackground = true,
    device = Devices.NEXUS_6
)
fun TrackBookScreen() {
    BookProgressSection()
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

            Icon(imageVector = Icons.Rounded.Info, contentDescription ="" )
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
