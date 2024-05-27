package com.dev.james.my_library.presentation.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.BookDetailsDialog
import com.dev.james.booktracker.compose_ui.ui.components.LibraryBookCardComponent
import com.dev.james.booktracker.compose_ui.ui.components.PortraitBooksRowComponent
import com.dev.james.booktracker.compose_ui.ui.components.SearchTextFieldComponent
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.states.GoogleBottomSheetUiState
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.compose_ui.ui.theme.Theme
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.my_library.presentation.navigation.MyBookShelfScreenNavigator
import com.dev.james.my_library.presentation.ui.viewmodel.BookShelfUIState
import com.dev.james.my_library.presentation.ui.viewmodel.MyBookShelfViewModel
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
@Destination
@Composable
fun MyBookShelfScreen(
    viewModel: MyBookShelfViewModel = hiltViewModel(),
    navigator: MyBookShelfScreenNavigator
) {

    LaunchedEffect(key1 = true) {
        viewModel.onAction(
            MyBookShelfViewModel.BookShelfUIActions
                .FetchData
        )
    }

    val state = viewModel.screenState.collectAsStateWithLifecycle()
    val events =
        viewModel.screenEvents.collectAsStateWithLifecycle(initialValue = MyBookShelfViewModel.BookShelfUiEvents.Default)
    var selectedBookState by remember {
        mutableStateOf(BaseBookData())
    }
    var showBookDetailsDialog by remember {
        mutableStateOf(false)
    }


    when (val event = events.value) {
        is MyBookShelfViewModel.BookShelfUiEvents.Default -> {}
        is MyBookShelfViewModel.BookShelfUiEvents.NavigateBack -> {
            navigator.popBackStack()
        }

        is MyBookShelfViewModel.BookShelfUiEvents.NavigateToBookDetailsScreen -> {
            navigator.fromBookShelfToTrackBookScreen(
                bookId = event.baseBookData.id
            )
        }

        is MyBookShelfViewModel.BookShelfUiEvents.ShowBookDetailsDialog -> {
            //show book details dialog
            showBookDetailsDialog = true
            selectedBookState = event.baseBookData
        }

        is MyBookShelfViewModel.BookShelfUiEvents.CloseBookDetailsDialog -> {
            showBookDetailsDialog = false
            selectedBookState = BaseBookData()
        }
    }

    if (showBookDetailsDialog) {
        BookDetailsDialog(
            book = selectedBookState,
            onDismiss = {
                viewModel.onAction(
                    MyBookShelfViewModel.BookShelfUIActions.CloseBookDetailsDialog
                )
            }
        )
    }

    MyBookShelfStatelessScreen(
        state = state.value,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun MyBookShelfStatelessScreen(
    state: BookShelfUIState = BookShelfUIState(),
    textSearchFieldState: String = "",
    googleSearchFieldState: FormState<TextFieldState> = FormState(fields = listOf()),
    googleBottomSheetUiState: GoogleBottomSheetUiState = GoogleBottomSheetUiState.StandbyState,
    onAction: (MyBookShelfViewModel.BookShelfUIActions) -> Unit = {}
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
        sheetSwipeEnabled = false,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            StandardToolBar(
                title = {
                    Text(text = "My Book Shelf", style = BookAppTypography.headlineMedium)
                },
                navigate = {
                    onAction(MyBookShelfViewModel.BookShelfUIActions.NavigateBack)
                },
                showBackArrow = true,
                navActions = {
                    IconButton(
                        modifier = Modifier.clip(CircleShape),
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
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

                    Text(
                        text = "Search for your book",
                        style = BookAppTypography.headlineSmall,
                        textAlign = TextAlign.Start
                    )
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            coroutineScope.launch {
                                sheetState.hide()
                                //cancelQuery()
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

                /*GoogleBooksSearchBottomSheet(
                    context = context,
                    googleSearchBottomSheetUiState = googleBottomSheetUiState,
                    searchFieldState = searchFieldState,
                    onBookSelected = {
                        //onBookSelected(it)
                        //add a book to the wishlist section
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    },
                    searchForBook = {
                        //trigger search action
                        //onSearchBook(it)
                    }
                )*/
            }
        },
        // contentColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                // verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val searchFieldText = rememberSaveable {
                    mutableStateOf("")
                }

                SearchTextFieldComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = searchFieldText.value,
                    onTextChanged = {
                        searchFieldText.value = it
                        onAction(MyBookShelfViewModel.BookShelfUIActions.OnSearch(queryString = it))
                    },
                    onClearText = {
                        searchFieldText.value = ""
                        onAction(MyBookShelfViewModel.BookShelfUIActions.OnSearch(queryString = ""))
                    }
                )

                val boxAlignment = if(state.isLoading) Alignment.Center else Alignment.TopCenter
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(scrollState)
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = boxAlignment
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            strokeCap = StrokeCap.Round,
                            strokeWidth = 3.dp
                        )
                    } else {
                        if(state.isSearching){
                            SearchResultView(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = MaterialTheme.colorScheme.background) ,
                                isLoading = state.isSearchLoading ,
                                searchResult= state.searchResult ,
                                searchMessage = state.searchEmptyMessage ,
                                onBookSelected = {
                                    onAction(
                                        MyBookShelfViewModel.BookShelfUIActions.OpenBookDetailsScreen(it.baseData)
                                    )
                                }
                            )
                        }else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = MaterialTheme.colorScheme.background),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {

                                val sections = listOf(
                                    "Currently Reading",
                                    "Not Started",
                                    "Completed",
                                    "My Wish List"
                                )

                                if (state.currentlyReading.isNotEmpty()) {
                                    PortraitBooksRowComponent(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .padding(bottom = 6.dp),
                                        sectionTitle = sections[0],
                                        showAddButton = false,
                                        items = state.currentlyReading
                                    ) {
                                        LibraryBookCardComponent(
                                            image = it.baseData.image,
                                            title = it.baseData.title,
                                            author = it.baseData.authors,
                                            progress = it.progress,
                                            shouldShowProgress = true,
                                            onItemSelected = {
                                                onAction(
                                                    MyBookShelfViewModel.BookShelfUIActions.OpenBookDetailsScreen(
                                                        it.baseData
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }

                                if (state.notRead.isNotEmpty()) {
                                    PortraitBooksRowComponent(
                                        sectionTitle = sections[1],
                                        showAddButton = false,
                                        items = state.notRead
                                    ) {
                                        LibraryBookCardComponent(
                                            image = it.baseData.image,
                                            title = it.baseData.title,
                                            author = it.baseData.authors,
                                            shouldShowProgress = false,
                                            onItemSelected = {
                                                onAction(
                                                    MyBookShelfViewModel.BookShelfUIActions.OpenBookDetailsScreen(
                                                        it.baseData
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }

                                if (state.finishedReading.isNotEmpty()) {
                                    PortraitBooksRowComponent(
                                        sectionTitle = sections[2],
                                        items = state.finishedReading,
                                        showAddButton = false
                                    ) {
                                        LibraryBookCardComponent(
                                            image = it.baseData.image,
                                            title = it.baseData.title,
                                            author = it.baseData.authors,
                                            progress = it.progress,
                                            shouldShowProgress = true,
                                            onItemSelected = {
                                                onAction(
                                                    MyBookShelfViewModel.BookShelfUIActions.OpenBookDetailsScreen(
                                                        it.baseData
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }

                                if (state.wishList.isNotEmpty()) {
                                    PortraitBooksRowComponent(
                                        sectionTitle = sections[3],
                                        items = state.wishList,
                                        showAddButton = false
                                    ) {
                                        LibraryBookCardComponent(
                                            image = it.image,
                                            title = it.title,
                                            author = it.authors,
                                            shouldShowProgress = false,
                                            onItemSelected = {
                                                onAction(
                                                    MyBookShelfViewModel.BookShelfUIActions.ShowBookDetailsDialog(
                                                        it
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }

                            }
                        }

                    }
                }
            }

        }
    )


}

@Composable
private fun SearchResultView(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    searchMessage : String = "",
    searchResult: List<LibraryBookData> = emptyList() ,
    onBookSelected : (LibraryBookData) -> Unit = {}
) {
    val boxAlignment = if(isLoading) Alignment.Center else Alignment.TopCenter

    Box(modifier = modifier, contentAlignment = boxAlignment) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                strokeCap = StrokeCap.Round
            )
        } else {
            val gridHorizontalArrangement = if(searchMessage.isNotEmpty()) Arrangement.Center else Arrangement.Start
            val gridVerticalAlignment = if(searchMessage.isNotEmpty()) Arrangement.Center else Arrangement.Top
            //Toast.makeText(LocalContext.current , searchResult.toString() , Toast.LENGTH_SHORT).show()

            if(searchMessage.isNotEmpty()){
                Text(modifier = Modifier.fillMaxWidth() , text = searchMessage , style = BookAppTypography.bodyMedium , textAlign = TextAlign.Center)
            }else {
                LazyVerticalGrid(
                    modifier= Modifier.fillMaxWidth().height(600.dp) ,
                    columns = GridCells.Fixed(count = 3),
                    contentPadding = PaddingValues(start = 2.dp, end = 2.dp) ,
                    verticalArrangement = gridVerticalAlignment ,
                    horizontalArrangement = gridHorizontalArrangement
                ){
                    items(
                        searchResult,
                        key = {
                            it.baseData.id
                        }
                    ){
                        LibraryBookCardComponent(
                            image = it.baseData.image ,
                            title = it.baseData.title ,
                            author = it.baseData.authors ,
                            progress = it.progress ,
                            shouldShowProgress = it.progress > 1 ,
                            onItemSelected = {
                                onBookSelected(it)
                            }
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
private fun MyBookShelfScreenPreview() {
    BookTrackerTheme(theme = Theme.LIGHT_THEME.themeValue) {
        MyBookShelfStatelessScreen(
            state = BookShelfUIState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun MyBookShelfScreenDarkPreview() {
    BookTrackerTheme(theme = Theme.DARK_THEME.themeValue) {
        MyBookShelfStatelessScreen(
            state = BookShelfUIState(),
            onAction = {}
        )
    }
}