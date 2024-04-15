package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
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
    onBookSelected: () -> Unit = {},
    onViewBook : () -> Unit = {} ,
    onNavigate : () -> Unit = {} ,
    onDeleteBook : () -> Unit = {}
    // onShowOptions : () -> Unit = {}
) {
    var showPopupMenuState by remember {
        mutableStateOf(false)
    }
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoilImageComponent(
                modifier = Modifier
                    .width(50.dp)
                    .height(80.dp),
                image = bookData.image
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.width(230.dp),
                    text = bookData.title,
                    style = BookAppTypography.labelMedium,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = bookData.author,
                    style = BookAppTypography.bodySmall,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(2.dp))
                val pagesText = if(bookData.isPending) "${bookData.pages} pages" else "${bookData.pagesRead}/${bookData.pages} pages"
                Text(
                    modifier = Modifier.width(230.dp),
                    text = pagesText ,
                    style = BookAppTypography.bodySmall,
                    textAlign = TextAlign.Start
                )
                if(!bookData.isPending){
                    LinearProgressIndicator(
                        modifier = Modifier.width(230.dp),
                        progress = bookData.progress.toFloat() / 100,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    DoneOrReading(
                        isDone = bookData.progress >= 100
                    )
                }

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = showPopupMenuState,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    ) + expandHorizontally(
                        animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) {
                        it
                    },
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    ) + shrinkHorizontally {
                        it
                    }
                ) {
                    MenuPopUp(
                        isPending = bookData.isPending,
                        onDismiss = {
                            showPopupMenuState = false
                        } ,
                        onOpen = {
                            onNavigate()
                        } ,
                        onDelete = {
                            onDeleteBook()
                        } ,
                        onView = {
                            onViewBook()
                        }
                    )
                }

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = androidx.compose.ui.graphics.Color.Transparent
                    ),
                    onClick = { showPopupMenuState = true }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )

                }
            }

        }

    }

}

@Composable
@Preview
private fun DoneOrReading(
    isDone: Boolean = false
) {
    val (color, text) = if (isDone) Pair(
        first = SuccessColor,
        second = "Done"
    ) else Pair(first = MaterialTheme.colorScheme.secondary, second = "reading..")
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(15.dp),
        color = color
    ) {
        Row(
            modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isDone) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "",
                    tint = SurfaceLight
                )
            }
            Text(
                text = text,
                style = BookAppTypography.bodySmall,
                textAlign = TextAlign.End,
                color = SurfaceLight,
                fontSize = 10.sp
            )

        }
    }

}


@Composable
@Preview()
private fun MenuPopUp(
    isPending : Boolean = false ,
    onDismiss: () -> Unit = {},
    onOpen: () -> Unit = {},
    onView : () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Popup(
        alignment = Alignment.TopCenter,
        offset = IntOffset(x = 0, y = -8),
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary
                    ), shape = RoundedCornerShape(15.dp)
                ),
            shape = RoundedCornerShape(15.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Row(
                // modifier = Modifier.padding(6.dp) ,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = {
                        if(isPending) {
                            onView()
                        }else {
                            onOpen()
                        }
                    }) {
                    Text(
                        text = if(isPending)"View" else "Continue",
                        style = BookAppTypography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                TextButton(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = { onDelete() }) {
                    Text(
                        text = "Remove",
                        style = BookAppTypography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = { onDismiss() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }

}

@Preview()
@Composable
fun HorizontalBookCardComponentDarkPreview() {
    BookTrackerTheme(
        theme = Theme.DARK_THEME.themeValue
    ) {
        HorizontalBookCardComponent()
    }

}
