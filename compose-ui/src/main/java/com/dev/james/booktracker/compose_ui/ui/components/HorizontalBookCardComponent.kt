package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.compose_ui.ui.theme.SuccessColor
import com.dev.james.booktracker.compose_ui.ui.theme.SurfaceLight
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.core.common_models.LibraryBookData

@Preview(showBackground = true)
@Composable
fun HorizontalBookCardComponent(
    bookData: LibraryBookData = LibraryBookData(),
    onBookSelected : () -> Unit = {},
    onShowOptions : () -> Unit = {}
){
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoilImageComponent(
                modifier = Modifier
                    .width(50.dp)
                    .height(80.dp) ,
                image = bookData.image
            )

            Column(
                modifier = Modifier.weight(1f) ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.width(230.dp) ,
                    text = bookData.title ,
                    style = BookAppTypography.labelMedium ,
                    textAlign = TextAlign.Start ,
                    maxLines = 1 ,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookData.author ,
                    style = BookAppTypography.bodySmall ,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    modifier = Modifier.width(230.dp) ,
                    text = "${bookData.pagesRead}/${bookData.pages} pages" ,
                    style = BookAppTypography.bodySmall ,
                    textAlign = TextAlign.Start
                )
                LinearProgressIndicator(
                    modifier = Modifier.width(230.dp) ,
                    progress = bookData.progress.toFloat() / 100 ,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(5.dp))
                DoneOrReading(
                    isDone = bookData.progress >= 100
                )
            }

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                ),
                onClick = { onShowOptions() }
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "" ,
                    tint = MaterialTheme.colorScheme.secondary
                )

            }

        }

    }

}

@Composable
@Preview
private fun DoneOrReading(
    isDone : Boolean = false
){
    val (color , text) = if(isDone) Pair(first = SuccessColor , second = "Done") else Pair(first = MaterialTheme.colorScheme.secondary , second = "reading..")
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(15.dp) ,
        color = color
    ) {
        Row(
            modifier = Modifier.padding(start = 6.dp , end = 6.dp , top = 2.dp , bottom = 2.dp ) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(isDone){
                Icon(
                    modifier = Modifier.size(14.dp),
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "" ,
                    tint = SurfaceLight
                )
            }
            Text(
                text = text ,
                style = BookAppTypography.bodySmall ,
                textAlign = TextAlign.End ,
                color = SurfaceLight ,
                fontSize = 10.sp
            )

        }
    }

}

@Preview()
@Composable
fun HorizontalBookCardComponentDarkPreview(){
    BookTrackerTheme(
        theme = Theme.DARK_THEME.themeValue
    ) {
        HorizontalBookCardComponent()
    }

}
