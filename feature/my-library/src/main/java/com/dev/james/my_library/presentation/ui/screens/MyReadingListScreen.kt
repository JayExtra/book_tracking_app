package com.dev.james.my_library.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
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
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.components.ConfirmationPrompt
import com.dev.james.booktracker.compose_ui.ui.components.GoogleBooksSearchBottomSheet
import com.dev.james.booktracker.compose_ui.ui.components.HorizontalBookCardComponent
import com.dev.james.booktracker.compose_ui.ui.components.LibraryBookCardComponent
import com.dev.james.booktracker.compose_ui.ui.components.SingleActionConfirmationPrompt
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.states.GoogleBottomSheetUiState
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.my_library.presentation.navigation.ReadingListScreenNavigator
import com.dev.james.my_library.presentation.ui.viewmodel.ReadingListScreenViewModel
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

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
    val savedBooksState = viewModel.savedBooks.collectAsStateWithLifecycle()


    var selectedBook by remember {
        mutableStateOf(LibraryBookData())
    }

    val googleBottomSheetUiState by viewModel.googleBottomSheetSearchState.collectAsStateWithLifecycle()

    var sheetOptionsState by rememberSaveable {
        mutableStateOf(BookSheetOptions.ALL)
    }

    if(viewModel.pendingBookDialogState){
        SingleActionConfirmationPrompt(
            title = "Success!",
            body = "${selectedBook.title} successfully added!",
            showAnimation = true,
            confirmationText = "Done",
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.PENDING_BOOK_ADDED_DIALOG
                        )
                )
            }
        )
    }

    if (viewModel.pendingBookRemovedDialogState) {
        SingleActionConfirmationPrompt(
            title = "Success!",
            body = "${selectedBook.title} successfully removed!",
            showAnimation = true,
            confirmationText = "Done",
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.PENDING_BOOK_REMOVED
                        )
                )
            }
        )
    }

    if (viewModel.confirmRemovePendingBookDialogState) {
        ConfirmationPrompt(
            title = "Remove This Book?",
            showCheckBox = false,
            body = "Do you wish to remove ${selectedBook.title} from this list? ",
            confirmationText = "Remove",
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .RemoveBookFromApi(
                            bookId = selectedBook.id,
                            readingListId = readingListId
                        )
                )
            },
            onCancel = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.CONFIRM_REMOVING_PENDING_BOOK
                        )
                )
            }
        )
    }



    if (viewModel.deleteReadingListDialogState) {
        ConfirmationPrompt(
            title = "Delete Reading List",
            showCheckBox = false,
            body = "Do you wish to delete ${state.value.readingList.name}?",
            confirmationText = "Delete",
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DeleteReadingList(readingListId)
                )
            },
            onCancel = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.CONFIRM_DELETE_READING_LIST_DIALOG
                        )
                )
            }
        )
    }

    if (viewModel.deleteConfirmationDialogState) {
        SingleActionConfirmationPrompt(
            title = "Success",
            body = "${state.value.readingList.name} deleted!",
            showAnimation = true,
            confirmationText = "Done",
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.READING_LIST_DELETED_DIALOG
                        )
                )
                readingListScreenNavigator.navBackToLibraryScreen()
            }
        )
    }

    if (viewModel.deleteBookConfirmationDialogState) {
        ConfirmationPrompt(
            title = "Remove This Book?",
            showCheckBox = false,
            body = "Do you wish to remove ${selectedBook.title} from this list? ",
            confirmationText = "Remove",
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DeleteBook(
                            book = selectedBook,
                            readingListId = readingListId
                        )
                )
            },
            onCancel = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.CONFIRM_BOOK_DELETE_DIALOG
                        )
                )
            }
        )
    }

    if (viewModel.bookDeletedDialogState) {
        SingleActionConfirmationPrompt(
            title = "Success!",
            body = "${selectedBook.title} successfully removed!",
            showAnimation = true,
            confirmationText = "Done",
            onAccept = {
                viewModel.onAction(
                    action = ReadingListScreenViewModel.ReadingListScreenAction
                        .DismissDialog(
                            dialogType = ReadingListScreenViewModel.DialogType.BOOK_DELETED_DIALOG
                        )
                )
            }
        )
    }
    StatelessReadingListScreen(
        state = state.value,
        googleBottomSheetUiState = googleBottomSheetUiState,
        searchFieldState = viewModel.bottomSheetSearchFieldState,
        sheetOptionsState = sheetOptionsState ,
        savedBooks = savedBooksState.value,
        onSheetOptionSelected = {
                sheetOptionsState = it
        },
        cancelQuery = {
              viewModel.cancelQueryJob()
        },
        onNavigate = {
            readingListScreenNavigator.navBackToLibraryScreen()
        },
        onDeleteList = {
            viewModel.onAction(
                action = ReadingListScreenViewModel.ReadingListScreenAction
                    .ShowDialog(
                        dialogType = ReadingListScreenViewModel.DialogType.CONFIRM_DELETE_READING_LIST_DIALOG
                    )
            )
        },
        onDeleteBook = {
            selectedBook = it
            viewModel.onAction(
                action = ReadingListScreenViewModel.ReadingListScreenAction
                    .ShowDialog(
                        dialogType = ReadingListScreenViewModel.DialogType
                            .CONFIRM_BOOK_DELETE_DIALOG
                    )
            )
        } ,
        onBookSelected = {
            //add to pending books
            viewModel.onAction(
                action = ReadingListScreenViewModel.ReadingListScreenAction
                    .AddBookFromApi(
                        book = it ,
                        readingListId = readingListId
                    )
            )

        } ,
        onSearchBook = { query ->
            viewModel.searchForBook(query)
        } ,
        onRemovePendingBook = { pendingBook ->
            selectedBook = pendingBook
            viewModel.onAction(
                action = ReadingListScreenViewModel.ReadingListScreenAction
                    .ShowDialog(
                        dialogType = ReadingListScreenViewModel.DialogType.CONFIRM_REMOVING_PENDING_BOOK
                    )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun StatelessReadingListScreen(
    state: ReadingListScreenViewModel.ReadingListScreenState = ReadingListScreenViewModel.ReadingListScreenState(),
    savedBooks : List<BookSave> = emptyList() ,
    googleBottomSheetUiState: GoogleBottomSheetUiState = GoogleBottomSheetUiState.StandbyState,
    searchFieldState: FormState<TextFieldState> = FormState(fields = listOf()),
    sheetOptionsState : BookSheetOptions = BookSheetOptions.ALL ,
    onSheetOptionSelected : (BookSheetOptions) -> Unit = {},
    cancelQuery : () -> Unit = {} ,
    onNavigate: () -> Unit = {},
    onDeleteList: () -> Unit = {},
    onDeleteBook: (LibraryBookData) -> Unit = {},
    onBookSelected: (Book) -> Unit = {},
    onSearchBook: (String) -> Unit = {} ,
    onRemovePendingBook : (LibraryBookData) -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
        confirmValueChange = {
            it != SheetValue.Hidden
        }
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()


    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(0.dp),
        sheetTonalElevation = 3.dp,
        sheetSwipeEnabled = false ,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        topBar = {
            StandardToolBar(
                title = {
                    Text(text = state.readingList.name, style = BookAppTypography.labelLarge)
                },
                navigate = {
                    onNavigate()
                },

                showBackArrow = true,
                navActions = {
                    IconButton(
                        modifier = Modifier.clip(CircleShape),
                        onClick = { onDeleteList() }
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
        sheetDragHandle = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, start = 8.dp, bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    val barText = when(sheetOptionsState){
                        BookSheetOptions.ALL -> {
                            "Select where to add from"
                        }
                        BookSheetOptions.GOOGLE_SEARCH -> {
                            "Search"
                        }
                        BookSheetOptions.SAVED_BOOKS -> {
                            "Your saved books"
                        }
                    }
                    Text(barText, style = BookAppTypography.headlineSmall , textAlign = TextAlign.Start)
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            coroutineScope.launch {
                                sheetState.hide()
                                onSheetOptionSelected(BookSheetOptions.ALL)
                                cancelQuery()
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "close bottom sheet ",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                Divider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        },
        sheetContent = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
                    //.height(600.dp)
                ,
                contentAlignment = Alignment.Center
            ) {
                when(sheetOptionsState){
                    BookSheetOptions.ALL -> {
                        BookImportOptions(
                            modifier = Modifier.fillMaxWidth() ,
                            onOptionSelected = {
                                //update state
                                onSheetOptionSelected(it)
                            }
                        )
                    }
                    BookSheetOptions.GOOGLE_SEARCH -> {
                        GoogleBooksSearchBottomSheet(
                            context = context,
                            googleSearchBottomSheetUiState = googleBottomSheetUiState,
                            searchFieldState = searchFieldState,
                            onBookSelected = {
                                onBookSelected(it)
                            },
                            searchForBook = {
                                onSearchBook(it)
                            }
                        )
                    }
                    BookSheetOptions.SAVED_BOOKS -> {
                        SavedBooksSection(
                            books = savedBooks,
                            onSavedBookSelected = {

                            }
                        )
                    }
                }
            }


        },
        content = { paddingValues ->

            val alignment = if (state.isLoading) Alignment.Center else Alignment.TopCenter
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
                            books = state.readingList.books,
                            onDelete = {
                                onDeleteBook(it)
                            } ,
                            onAddBook = {
                                coroutineScope.launch {
                                    sheetState.expand()
                                }
                            }
                        )
                        if(state.pendingBooks.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Divider()
                            PendingBooksSection(
                                books = state.pendingBooks,
                                onRemoveBook = {
                                       onRemovePendingBook(it)
                                },
                                onView = {

                                }
                            )
                        }
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
private fun PendingBooksSection(
    modifier: Modifier = Modifier,
    books : List<LibraryBookData> ,
    errorMessage : String = "",
    onRemoveBook : (LibraryBookData) -> Unit ,
    onView : (LibraryBookData) -> Unit
){
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        if(errorMessage.isNotEmpty()){
            Text(text = errorMessage , style = BookAppTypography.bodySmall , textAlign = TextAlign.Center)
        }else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ){
                items(items = books , key = { it.id }){ book ->
                    HorizontalBookCardComponent(
                        bookData = book,
                        onDeleteBook = {
                            //delete book from vm
                            onRemoveBook(book)
                        },
                        onViewBook = {
                            //navigate to book data screen
                            onView(book)
                        }
                    )
                }

            }
        }
    }

}


@Composable
private fun BooksSection(
    modifier: Modifier = Modifier,
    books: List<LibraryBookData> = emptyList(),
    onDelete: (LibraryBookData) -> Unit = {},
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
                        bookData = book,
                        onDeleteBook = {
                            //delete book from vm
                            onDelete(book)
                        },
                        onNavigate = {
                            //navigate to book data screen
                        }
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

@Preview(showBackground = true)
@Composable
private fun BookImportOptions(
    modifier: Modifier = Modifier ,
    onOptionSelected : (BookSheetOptions) -> Unit = {}
){
    Surface(modifier = modifier.wrapContentHeight()) {
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    .clickable(
                        onClick = {
                            onOptionSelected(BookSheetOptions.SAVED_BOOKS)
                        }
                    ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        painter = painterResource(id = R.drawable.ic_saved_book),
                        contentDescription = "" ,
                        tint = MaterialTheme.colorScheme.secondary
                        )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(text = "Saved books" , style = BookAppTypography.labelMedium , color = MaterialTheme.colorScheme.secondary)
                }
            }

            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    .clickable(
                        onClick = {
                            onOptionSelected(BookSheetOptions.GOOGLE_SEARCH)
                        }
                    ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "" ,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(text = "Search Online" , style = BookAppTypography.labelMedium , color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }

}

@Composable
private fun SavedBooksSection(
    modifier: Modifier = Modifier ,
    books : List<BookSave> = emptyList() ,
    onSavedBookSelected : (BookSave) -> Unit = {}
){
    val alignment = if(books.isEmpty()) Alignment.Center else Alignment.TopCenter
    Box(
        modifier = modifier.padding(8.dp),
        contentAlignment = alignment
    ) {
       if(books.isEmpty()){
           Text(
               modifier = Modifier.fillMaxWidth() ,
               text = "No books currently added." ,
               style = BookAppTypography.labelMedium ,
               )
       }else {
           LazyVerticalGrid(
               modifier = Modifier.fillMaxWidth() ,
               columns = GridCells.Fixed(3) ,
               contentPadding = PaddingValues(6.dp) ,
               verticalArrangement = Arrangement.spacedBy(10.dp) ,
               horizontalArrangement = Arrangement.Center
           ){
               items(items = books , key = { it.bookId }){ book ->
                   LibraryBookCardComponent(
                       image = book.bookImage ,
                       title = book.bookTitle,
                       author = book.bookAuthors,
                       shouldShowProgress = false ,
                       onItemSelected = {
                           onSavedBookSelected(book)
                       }
                   )
               }

           }
       }
    }
}


enum class BookSheetOptions{
    ALL ,
    SAVED_BOOKS ,
    GOOGLE_SEARCH
}


//data class YOffset(val value : Dp)

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