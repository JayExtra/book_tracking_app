package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.utilities.convertToAuthorsString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun BookCardComponent(
    modifier: Modifier = Modifier,
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

        Column(
            modifier = Modifier.fillMaxSize().padding(start = 0.dp, end = 0.dp)
        ) {

            CoilImageComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
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
}