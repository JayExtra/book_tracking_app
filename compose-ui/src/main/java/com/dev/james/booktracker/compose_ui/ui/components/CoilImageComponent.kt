package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.dev.james.booktracker.compose_ui.R

@Composable
fun CoilImageComponent(
    modifier : Modifier = Modifier,
    image : String = ""
){
    val painter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalContext.current)
            .data(data = image)
            .apply(block = {
                placeholder(R.drawable.image_placeholder_24)
                error(R.drawable.image_placeholder_24)
                crossfade(true)
                transformations(
                    RoundedCornersTransformation(0f)
                )
            }).build()
    )

    val painterState = painter.state

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "book thumbnail",
            contentScale = ContentScale.Crop ,
            modifier = Modifier.fillMaxSize()
        )
        if (painterState is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}