package com.dev.james.booktracker.compose_ui.ui.components.bottom_sheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.components.BookCardComponent
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.utilities.convertToAuthorsString

@Composable
fun PdfListBottomSheetContent(
    modifier : Modifier = Modifier,
    pdfBookItems : List<Book> = listOf(),
    isGrid : Boolean = true,
    onPdfBookSelected : (Book) -> Unit = {}
){
    Column(modifier = modifier.fillMaxSize()) {
        if(isGrid){
            LazyVerticalGrid(
                columns = GridCells.Adaptive( minSize = 80.dp) ,
                contentPadding = PaddingValues(horizontal = 5.dp , vertical = 5.dp) ,
                horizontalArrangement = Arrangement.spacedBy(5.dp) ,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ){

                items(pdfBookItems){ book ->
                    BookContent(
                        book = book ,
                        onBookSelected = {
                            onPdfBookSelected(book)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookContent(
    book : Book = Book() ,
    onBookSelected : (Book) -> Unit
){
    BookCardComponent(
        modifier = Modifier.width(120.dp).height(160.dp) ,
        book = book ,
        onBookSelected = {
            onBookSelected(book)
        }
    )
}


@Composable
@Preview(showBackground = true , widthDp = 220  , heightDp = 720 )
fun PdfListBottomSheetPreview(){
    PdfListBottomSheetContent(
        pdfBookItems = listOf(
            Book(bookImage = "" , bookTitle = "Title A" , bookAuthors = listOf("Author A")) ,
            Book(bookImage = "" , bookTitle = "Title B" , bookAuthors = listOf("Author B")) ,
            Book(bookImage = "" , bookTitle = "Title C" , bookAuthors = listOf("Author C")) ,
            Book(bookImage = "" , bookTitle = "Title D" , bookAuthors = listOf("Author D")) ,
            Book(bookImage = "" , bookTitle = "Title E" , bookAuthors = listOf("Author E")) ,
            Book(bookImage = "" , bookTitle = "Title F" , bookAuthors = listOf("Author F")) ,
        )
    )
}