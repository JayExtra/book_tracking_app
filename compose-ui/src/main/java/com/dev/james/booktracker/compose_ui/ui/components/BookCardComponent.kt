package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.enums.Orientation
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.utilities.convertToAuthorsString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun BookCardComponent(
    modifier: Modifier = Modifier,
    orientation : Orientation = Orientation.PORTRAIT ,
    book: Book = Book(),
    onBookSelected: (Book) -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        onClick = {
            onBookSelected(book)
        }
    ) {

        when (orientation) {
            Orientation.PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                ) {

                    CoilImageComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        image = book.bookImage ?: ""
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Column(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .fillMaxWidth()) {
                        Text(
                            text = book.bookTitle ?: "No title found",
                            style = BookAppTypography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = book.bookAuthors?.convertToAuthorsString() ?: "No author found",
                            style = BookAppTypography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontSize = 10.sp
                        )
                    }

                }
            }
            Orientation.LANDSCAPE -> {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(2.dp) ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CoilImageComponent(
                        modifier = Modifier
                            .width(60.dp)
                            .height(80.dp),
                        image = book.bookImage ?: ""
                    )
                    Column(
                        modifier = Modifier.padding(start = 4.dp , end = 4.dp) ,
                        verticalArrangement = Arrangement.Top ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = book.bookTitle ?: "No title found",
                            style = BookAppTypography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = book.bookAuthors?.convertToAuthorsString() ?: "No author found",
                            style = BookAppTypography.labelSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontSize = 10.sp
                        )
                    }
                }

            }
            else -> { }
        }
    }
}