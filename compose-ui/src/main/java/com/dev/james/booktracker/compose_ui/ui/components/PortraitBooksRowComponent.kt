package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@Preview(showBackground = true)
@Composable
fun <T> PortraitBooksRowComponent(
    modifier: Modifier = Modifier,
    sectionTitle : String = "",
    showAddButton : Boolean = true ,
    items: List<T> = listOf<T>(),
    onSeeAllClicked: () -> Unit = {},
    onAddMoreClicked: () -> Unit = {} ,
    itemContent : @Composable (T) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = sectionTitle,
            style = BookAppTypography.headlineSmall,
            fontWeight = FontWeight.Bold ,
            textAlign = TextAlign.Start
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = if (items.isEmpty()) Arrangement.Center else Arrangement.spacedBy(
                12.dp
            )
        ) {
            if (items.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            space = 5.dp,
                            alignment = Alignment.CenterVertically
                        )
                    ) {
                        Text(
                            modifier = Modifier.width(200.dp),
                            text = "There are no books you are currently reading. Proceed to add one?",
                            style = BookAppTypography.bodyMedium,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            modifier = Modifier.wrapContentWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            onClick = { onAddMoreClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                modifier = Modifier.padding(start = 3.dp),
                                text = "add a book",
                                style = BookAppTypography.labelMedium,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

            } else {

                items(items = items) {  item ->
                    itemContent(item)
                }

                if(showAddButton) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .wrapContentWidth()
                                .padding(start = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            FloatingActionButton(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape,
                                elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 0.dp
                                ),
                                onClick = { onAddMoreClicked() }
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "")
                            }
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = "add more",
                                style = BookAppTypography.labelSmall,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }


            }


        }
    }

}