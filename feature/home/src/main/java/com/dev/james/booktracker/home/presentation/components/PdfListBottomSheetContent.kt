package com.dev.james.booktracker.home.presentation.components

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.BookCardComponent
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.home.presentation.viewmodels.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PdfListBottomSheetContent(
    modifier : Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    isExpanded : Boolean = false ,
    isGrid : Boolean = true,
    onPdfBookSelected : (Book) -> Unit = {}
){

    if(isExpanded){
        homeScreenViewModel.getCachedPdfs()
    }

    val pdfBookItemsListState = homeScreenViewModel.pdfBookListFlow.collectAsStateWithLifecycle()


    Box(modifier = modifier.fillMaxSize()) {
        if(pdfBookItemsListState.value[0].bookUri == Uri.EMPTY){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center) ,
                color = MaterialTheme.colorScheme.primary ,
                strokeWidth = 5.dp
            )
        }else {
            if(isGrid){
                LazyVerticalGrid(
                    columns = GridCells.Adaptive( minSize = 80.dp) ,
                    contentPadding = PaddingValues(horizontal = 5.dp , vertical = 5.dp) ,
                    horizontalArrangement = Arrangement.spacedBy(5.dp) ,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ){

                    items(pdfBookItemsListState.value){ book ->
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
}

@Composable
fun BookContent(
    book : Book = Book() ,
    onBookSelected : (Book) -> Unit
){
    BookCardComponent(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp) ,
        book = book ,
        onBookSelected = {
            onBookSelected(book)
        }
    )
}


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
@Preview(showBackground = true , widthDp = 220  , heightDp = 720 )
fun PdfListBottomSheetPreview(){
    PdfListBottomSheetContent()
}