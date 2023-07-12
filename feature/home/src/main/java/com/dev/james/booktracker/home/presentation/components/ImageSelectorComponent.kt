package com.dev.james.booktracker.home.presentation.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.viewmodels.ImageSelectorUiState

@Composable
@Preview("ImageSelectorComponent")
fun ImageSelectorComponent(
    imageSelectorState: ImageSelectorUiState = ImageSelectorUiState(),
    onSelect: () -> Unit = {} ,
    onClear : () -> Unit = {}
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(140.dp)
            .height(191.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .border(
                width = if (imageSelectorState.isError) 2.dp else 0.dp,
                color = if (imageSelectorState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                //starts the image picking flow
                onSelect()
            }
    ) {

        if (imageSelectorState.imageSelectedUri != Uri.EMPTY || imageSelectorState.imageUrl.isNotBlank() ) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = if (imageSelectorState.imageSelectedUri != Uri.EMPTY) imageSelectorState.imageSelectedUri else imageSelectorState.imageUrl
                ),
                contentDescription = "taken book image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = {
                //clear image
                    onClear()
                 } ,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.secondary
                    )
                    .size(30.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear image icon")
            }
        }

        if (imageSelectorState.imageSelectedUri == Uri.EMPTY && !imageSelectorState.showProgress && imageSelectorState.imageUrl.isBlank()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(id = R.drawable.add_a_photo_24),
                    contentDescription = "Add image placeholder",
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                        .padding(10.dp),
                    tint = MaterialTheme.colorScheme.secondary

                )
                Text(
                    text = if (imageSelectorState.isError) "Add image*" else "Add image",
                    style = BookAppTypography.labelLarge,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = if (imageSelectorState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )
            }
        }

        if (imageSelectorState.showProgress) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }

}