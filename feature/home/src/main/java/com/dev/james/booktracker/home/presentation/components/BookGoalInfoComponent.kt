package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R

@Composable
@Preview(showBackground = true)
fun BookGoalInfoComponent(){
    
    Column(
        modifier = Modifier.fillMaxWidth() ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.current_read) ,
            style = BookAppTypography.labelLarge
        )
    }
}

@Composable
fun GoalDataComponent(){
    //use constraint layout
    val constraints = ConstraintSet {
        val imageSection = createRefFor("image_section")
        val progressSection = createRefFor("progress_section")
        val chapterSection = createRefFor("chapter_section")
        val bestTimeSection = createRefFor("best_time_section")
    }

}

@Composable
fun ProgressComponent(){

}



@Composable
fun BookImageComponent(
    image : String = "" ,
    isUri : Boolean = false
){

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

    Box(modifier = Modifier
        .height(120.dp)
        .width(78.dp)
        .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "book thumbnail" ,
            contentScale = ContentScale.Fit
        )
        if(painterState is AsyncImagePainter.State.Loading){
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
    
}
