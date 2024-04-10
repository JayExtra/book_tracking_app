package com.dev.james.my_library.presentation.ui.screens

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import com.dev.james.booktracker.compose_ui.R
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.components.ConfirmationPrompt
import com.dev.james.booktracker.compose_ui.ui.components.HorizontalBookCardComponent
import com.dev.james.booktracker.compose_ui.ui.components.SingleActionConfirmationPrompt
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.my_library.presentation.navigation.ReadingListScreenNavigator
import com.dev.james.my_library.presentation.ui.viewmodel.ReadingListScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun ReadingListScreen(
    readingListId: String,
    readingListScreenNavigator: ReadingListScreenNavigator,
    viewModel: ReadingListScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.onAction(
            action = ReadingListScreenViewModel.ReadingListScreenAction
                .FetchReadingList(readingListId)
        )
    }

    val state = viewModel.state.collectAsStateWithLifecycle()


    if(viewModel.deleteReadingListDialogState){
        ConfirmationPrompt(
            title = "Delete Reading List" ,
            showCheckBox = false ,
            body = "Do you wish to delete ${state.value.readingList.name}?" ,
            confirmationText = "Delete",
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DeleteReadingList(readingListId)
                )
            } ,
            onCancel = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.CONFIRM_DELETE_DIALOG
                        )
                )
            }
        )
    }

    if(viewModel.deleteConfirmationDialogState){
        SingleActionConfirmationPrompt(
            title = "Success",
            body = "${state.value.readingList.name} deleted!" ,
            showAnimation = true ,
            confirmationText = "Done" ,
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.DELETED_DIALOG
                        )
                )
                readingListScreenNavigator.navBackToLibraryScreen()
            }
        )
    }
    StatelessReadingListScreen(
        state = state.value,
        onNavigate = {
            readingListScreenNavigator.navBackToLibraryScreen()
        } ,
        onDeleteList = {
            viewModel.onAction(
                action = ReadingListScreenViewModel.ReadingListScreenAction
                    .ShowDialog(
                        dialogType = ReadingListScreenViewModel.DialogType.CONFIRM_DELETE_DIALOG
                    )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessReadingListScreen(
    state: ReadingListScreenViewModel.ReadingListScreenState = ReadingListScreenViewModel.ReadingListScreenState(),
    onNavigate: () -> Unit = {} ,
    onDeleteList : () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            StandardToolBar(
                title = {
                    Text(text = "Dummy Title")
                },
                navigate = {
                    onNavigate()
                },

                showBackArrow = true,
                navActions = {
                    IconButton(
                        modifier = Modifier.clip(CircleShape),
                        onClick = { onDeleteList()}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        },
        content = { paddingValues ->

           val alignment =  if(state.isLoading) Alignment.Center else Alignment.TopCenter
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState),
                contentAlignment = alignment
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        strokeCap = StrokeCap.Round,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BookDetailsSection(
                            readingListItem = state.readingList
                        )
                        Divider()
                        Spacer(modifier = Modifier.height(4.dp))
                        BooksSection(
                            books = state.readingList.books
                        )
                    }
                }

            }
        }
    )
}

@Composable
private fun BookDetailsSection(
    modifier: Modifier = Modifier,
    readingListItem: ReadingLists = ReadingLists(),
    onAddImage: () -> Unit = {}
) {
    val completedBooks =
        readingListItem.books.filter {
            it.progress >= 100
        }.size

    val totalBooks = readingListItem.books.size

    val pluralText = if (totalBooks == 1) "book" else "books"


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ImageSection(
            imagesList = readingListItem.books.map { it.image },
            onAddImage = {
                onAddImage()
            }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = readingListItem.name,
                style = BookAppTypography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.width(350.dp),
                text = readingListItem.description,
                style = BookAppTypography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "$totalBooks $pluralText . $completedBooks completed",
                style = BookAppTypography.bodySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = readingListItem.date,
                style = BookAppTypography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun BooksSection(
    modifier: Modifier = Modifier,
    books: List<LibraryBookData> = emptyList(),
    onDelete: (String) -> Unit = {},
    onAddBook: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                modifier = Modifier.size(50.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(5.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp
                ),
                onClick = {
                    onAddBook()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier.weight(2f),
                text = "Add a book",
                style = BookAppTypography.labelMedium,
                textAlign = TextAlign.Start
            )
        }

        val verticalArrangement =
            if (books.isEmpty()) Arrangement.Center else Arrangement.spacedBy(6.dp)

        LazyColumn(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (books.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No books added in this reading list.",
                        style = BookAppTypography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(
                    items = books,
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
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    imagesList: List<String> = emptyList(),
    onAddImage: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {

            if (imagesList.isEmpty()) {
                Icon(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = R.drawable.ic_image),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            } else if (imagesList.size == 1) {
                CoilImageComponent(
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp),
                    image = imagesList[0]
                )
            } else {
                LazyVerticalGrid(
                    //  modifier = Modifier.size(200.dp) ,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                    userScrollEnabled = false
                ) {
                    items(count = 4) { index ->
                        val imageUrl = imagesList[index % imagesList.size]
                        CoilImageComponent(
                            modifier = Modifier
                                .width(90.dp)
                                .height(120.dp),
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
                .background(color = Color.Transparent),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(
                modifier = Modifier.clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = { onAddImage() }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.background
                )
            }

        }
    }

}

@Preview
@Composable
private fun ReadingListScreenPreview() {
    BookTrackerTheme {
        StatelessReadingListScreen()
    }
}

@Preview
@Composable
private fun ReadingListScreenDarkPreview() {
    BookTrackerTheme(
        theme = Theme.DARK_THEME.themeValue
    ) {
        StatelessReadingListScreen()
    }
}