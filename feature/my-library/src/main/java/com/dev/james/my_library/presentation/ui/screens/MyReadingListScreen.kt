package com.dev.james.my_library.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import com.dev.james.booktracker.compose_ui.R
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.components.HorizontalBookCardComponent
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppShapes
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.my_library.presentation.ui.viewmodel.ReadingListScreenViewModel

@Composable
fun ReadingListScreen(
    readingListId : String ,
    viewModel : ReadingListScreenViewModel = hiltViewModel()
){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessReadingListScreen(){
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize() ,
        topBar = {
            StandardToolBar(
                title = {
                    Text(text = "Dummy Title")
                },
                showBackArrow = true,
                navActions = {
                    IconButton(
                        modifier = Modifier.clip(CircleShape) ,
                        onClick = { /*TODO 1 : Delete reading list action*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "" ,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        } ,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp) ,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    BookDetailsSection()
                    Divider()
                    Spacer(modifier = Modifier.height(4.dp))
                    BooksSection()

                }
            }
        }
    )
}

@Composable
private fun BookDetailsSection(
    modifier: Modifier = Modifier ,
    readingListItem : ReadingLists = ReadingLists() ,
    onAddImage: () -> Unit = {}
){
    val completedBooks =
            readingListItem.readingList.filter {
                it.progress >= 100
            }.size

    val totalBooks = readingListItem.readingList.size

    val pluralText = if(totalBooks == 1) "book" else "books"


    Column(
        modifier = modifier ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ImageSection(
            imagesList = readingListItem.readingList.map { it.image } ,
            onAddImage = {
                onAddImage()
            }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = readingListItem.name ,
                style = BookAppTypography.headlineLarge ,
                fontWeight = FontWeight.Bold ,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = readingListItem.description ,
                style = BookAppTypography.bodyMedium ,
                fontWeight = FontWeight.Medium ,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "$totalBooks $pluralText . $completedBooks read" ,
                style = BookAppTypography.bodySmall ,
                fontWeight = FontWeight.Bold ,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = readingListItem.date ,
                style = BookAppTypography.labelSmall ,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun BooksSection(
    modifier  : Modifier = Modifier ,
    books : List<LibraryBookData> = emptyList() ,
    onDelete : (String) -> Unit = {} ,
    onAddBook : () -> Unit = {}
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                modifier = Modifier.size(50.dp),
                containerColor =  MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(5.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp
                ),
                onClick = {
                    onAddBook()
                }
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier.weight(2f),
                text = "Add a book" ,
                style = BookAppTypography.labelMedium ,
                textAlign = TextAlign.Start
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(books.isEmpty()){
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No books added in this reading list." ,
                        style = BookAppTypography.labelMedium ,
                        textAlign = TextAlign.Center
                    )
                }
            }else {
                items(
                    items = books ,
                    key = {
                        it.id
                    }
                ) { book ->
                    HorizontalBookCardComponent(
                        bookData = book
                    )
                }

            }
        }

    }

}



@Composable
private fun ImageSection(
    modifier : Modifier = Modifier ,
    imagesList : List<String> = emptyList() ,
    onAddImage : () -> Unit = {}
){
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(5.dp)) ,
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .size(200.dp)
        ) {
            val numColumns =
                when (imagesList.size) {
                    0 -> 1
                    1 -> 1
                    else -> 2
                }
            LazyVerticalGrid(
                modifier = Modifier.size(200.dp) ,
                columns = GridCells.Fixed(numColumns) ,
                verticalArrangement = Arrangement.Center
            ){
                if(imagesList.isEmpty()){
                    item {
                        Icon(
                            modifier = Modifier.size(100.dp),
                            painter = painterResource(id = R.drawable.ic_image),
                            contentDescription ="" ,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    items(count = 4){ index ->
                        val imageUrl = if(imagesList.size == 1) imagesList[0] else imagesList[index % imagesList.size]
                        CoilImageComponent(
                            modifier = Modifier
                                .width(90.dp)
                                .height(120.dp) ,
                            image = imageUrl
                        )
                    }
                }

            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .background(color = Color.Transparent) ,
            contentAlignment = Alignment.BottomEnd
        ){
            IconButton(
                modifier = Modifier.clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = { onAddImage() }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "" ,
                    tint = MaterialTheme.colorScheme.background
                )
            }

        }
    }

}

@Preview
@Composable
private fun ReadingListScreenPreview(){
    BookTrackerTheme {
        StatelessReadingListScreen()
    }
}

@Preview
@Composable
private fun ReadingListScreenDarkPreview(){
    BookTrackerTheme(
        theme = Theme.DARK_THEME.themeValue
    ) {
        StatelessReadingListScreen()
    }
}