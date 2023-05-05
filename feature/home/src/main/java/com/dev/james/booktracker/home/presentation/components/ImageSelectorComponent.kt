package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.viewmodels.ImageSelectorUiState

@Composable
@Preview("ImageSelectorComponent")
fun ImageSelectorComponent(
    imageSelectorState: ImageSelectorUiState = ImageSelectorUiState(),
    onSelect: () -> Unit = {}
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(158.dp)
            .height(191.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .border(
                width = if (imageSelectorState.isError) 2.dp else 0.dp,
                color = if (imageSelectorState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                //starts the image picking flow
                onSelect()
            }
    ) {

        if (imageSelectorState.imageSelectedUri.isNotBlank()) {
            //will replace with coil
            Image(
                painterResource(id = R.drawable.image_placeholder_24),
                contentDescription = "taken book image",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )
        }

        if (imageSelectorState.imageSelectedUri.isBlank() && !imageSelectorState.showProgress) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(id = R.drawable.add_a_photo_24),
                    contentDescription = "Add image placeholder",
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp),
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