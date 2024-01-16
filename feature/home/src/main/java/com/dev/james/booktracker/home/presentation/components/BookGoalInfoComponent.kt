package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.utilities.formatTimeToDHMS
import com.dev.james.booktracker.home.R

@Composable
@Preview(showBackground = true)
fun BookGoalInfoComponent(
    shouldNotShowBlankMessage : Boolean = false,
    bookProgressData: BookProgressData = BookProgressData(),
    onContinueClicked : () -> Unit = {} ,
    onProceedToMyLibrary : () -> Unit = {}
) {

    Card(
        modifier = Modifier.fillMaxWidth() ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ) ,
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = if(shouldNotShowBlankMessage) Arrangement.Center else Arrangement.Top
        ) {
            if(shouldNotShowBlankMessage){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.current_read),
                    style = BookAppTypography.labelLarge,
                    textAlign = TextAlign.Start
                )
                GoalDataComponent(
                    bookProgressData = bookProgressData ,
                    onContinue = {
                        onContinueClicked()
                    }
                )

            }else {

                Text(
                    text = "No reading progress currently available. Go to my library and start reading a book." ,
                    style = BookAppTypography.bodyMedium ,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                ElevatedButton(onClick = { onProceedToMyLibrary() } , colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )) {
                    Text(text = "proceed" , style = BookAppTypography.labelSmall)
                }


            }

        }

    }
}

@Composable
@Preview(showBackground = true)
fun GoalDataComponent(
    bookProgressData: BookProgressData = BookProgressData(),
    onContinue : () -> Unit = {}
) {
    //use constraint layout
    val constraints = ConstraintSet {
        val imageSection = createRefFor("image_section")
        val progressSection = createRefFor("progress_section")
        val chapterSection = createRefFor("chapter_section")
        val bestTimeSection = createRefFor("best_time_section")
        val continueBtn = createRefFor("continue_button")

        constrain(imageSection) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
        }
        constrain(progressSection) {
            start.linkTo(imageSection.end, 8.dp)
            top.linkTo(imageSection.top)
            end.linkTo(parent.end)
        }
        constrain(chapterSection) {
            start.linkTo(progressSection.start)
            top.linkTo(progressSection.bottom, 9.dp)
        }
        constrain(bestTimeSection) {
            start.linkTo(chapterSection.start)
            top.linkTo(chapterSection.bottom, 9.dp)
        }
        constrain(continueBtn){
            start.linkTo(bestTimeSection.start)
            top.linkTo(bestTimeSection.bottom , 15.dp)
           // end.linkTo(parent.end)
        }
    }
    ConstraintLayout(
        constraints,
        Modifier.fillMaxWidth()
    ) {
        BookImageComponent(
            modifier = Modifier.layoutId("image_section"),
            image = bookProgressData.bookImage,
            isUri = bookProgressData.isUri
        )
        ProgressComponent(
            modifier = Modifier.layoutId("progress_section"),
            progress = bookProgressData.progress
        )

        ReadProgressComponent(
            modifier = Modifier.layoutId("chapter_section"),
            icon = R.drawable.ic_bookmark_24,
            title = "Chapter ${bookProgressData.currentChapter}",
            subTitle = bookProgressData.currentChapterTitle
        )
        ReadProgressComponent(
            modifier = Modifier.layoutId("best_time_section"),
            icon = R.drawable.ic_clock_24,
            title = "Total time",
            subTitle = bookProgressData.totalTimeSpent.formatTimeToDHMS()
        )

        ElevatedButton(
            modifier = Modifier
                .layoutId("continue_button")
                .width(250.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                //open book lo screen
                onContinue()
            }) {
            Text(
               // modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.continue_reading) ,
                style = BookAppTypography.labelMedium ,
                textAlign = TextAlign.Center
            )
        }

    }

}

@Composable
@Preview(showBackground = true)
fun ProgressComponent(
    modifier: Modifier = Modifier,
    progress: Float = 0f
) {

    val constraints = ConstraintSet {
        val progressLabel = createRefFor("progress_label")
        val progressBar = createRefFor("progress_bar")
        val progressCircle = createRefFor("progress_circle")

        constrain(progressLabel) {
            top.linkTo(parent.top)
            start.linkTo(parent.start, 8.dp)
        }

        constrain(progressBar) {
            start.linkTo(progressLabel.start)
            top.linkTo(progressLabel.bottom, margin = 8.dp)
        }

        constrain(progressCircle) {
            start.linkTo(progressBar.end, margin = 8.dp)
            top.linkTo(progressBar.top)
            bottom.linkTo(progressBar.bottom)
        }
    }

    ConstraintLayout(
        constraintSet = constraints,
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.layoutId("progress_label"),
            text = "progress",
            style = BookAppTypography.labelLarge
        )
        LinearProgressIndicator(
            modifier = Modifier
                .layoutId("progress_bar")
                .width(200.dp)
                .height(5.dp),
            color = MaterialTheme.colorScheme.primary,
            progress = progress ,
            strokeCap = StrokeCap.Round
        )
        Box(
            contentAlignment = Alignment.Center, modifier =
            Modifier
                .layoutId("progress_circle")
                .size(30.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primary)

        ) {
            Text(
                text = "${progress.toInt()}%",
                color = Color.White,
                style = BookAppTypography.bodySmall,
                fontSize = 9.sp
            )
        }

    }

}

@Composable
@Preview(showBackground = true)
fun ReadProgressComponent(
    modifier: Modifier = Modifier,
    title: String = "Some title",
    subTitle: String = "Some chapter",
    icon: Int = R.drawable.ic_clock_24
) {
    val constraints = ConstraintSet {

        val titleComponent = createRefFor("title_label")
        val messageComponent = createRefFor("message_label")
        val iconComponent = createRefFor("icon_label")

        constrain(iconComponent) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
        }
        constrain(titleComponent) {
            start.linkTo(iconComponent.end, 4.dp)
            top.linkTo(iconComponent.top)
        }
        constrain(messageComponent) {
            start.linkTo(titleComponent.start)
            top.linkTo(titleComponent.bottom)
        }
    }

    ConstraintLayout(
        constraints,
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.layoutId("title_label"),
            text = title,
            style = BookAppTypography.headlineSmall,
            fontSize = 12.sp
        )
        Text(
            modifier = Modifier.layoutId("message_label"),
            text = subTitle,
            style = BookAppTypography.bodySmall,
            fontSize = 16.sp
        )
        Icon(
            modifier = Modifier
                .layoutId("icon_label")
                .size(30.dp),
            painter = painterResource(id = icon), contentDescription = ""
        )
    }


}


@Composable
fun BookImageComponent(
    modifier: Modifier = Modifier,
    image: String = "",
    isUri: Boolean = false
) {

    val painter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalContext.current)
            .data(data = image)
            .apply(block = {
                //placeholder(R.drawable.image_placeholder_24)
                error(R.drawable.image_placeholder_24)
                crossfade(true)
                transformations(
                    RoundedCornersTransformation(0f)
                )
            }).build()
    )

    val painterState = painter.state

    Box(
        modifier = modifier
            .height(150.dp)
            .width(100.dp)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "book thumbnail",
            contentScale = ContentScale.Crop
        )
        if (painterState is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }

}
