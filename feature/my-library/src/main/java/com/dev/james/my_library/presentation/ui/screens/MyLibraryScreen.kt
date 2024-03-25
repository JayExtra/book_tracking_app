package com.dev.james.my_library.presentation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.dev.james.booktracker.compose_ui.R
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.ImageSelectorUiState
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.components.ImageSelectorComponent
import com.dev.james.booktracker.compose_ui.ui.components.LibraryBookCardComponent
import com.dev.james.booktracker.compose_ui.ui.components.OutlinedTextFieldComponent
import com.dev.james.booktracker.compose_ui.ui.enums.PreviousScreenDestinations
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.booktracker.core.common_models.SuggestedBook
import com.dev.james.my_library.presentation.navigation.MyLibraryScreenNavigator
import com.dev.james.my_library.presentation.ui.components.SuggestedBookCardComponent
import com.dev.james.my_library.presentation.ui.viewmodel.MyLibraryViewModel
import com.ramcosta.composedestinations.annotation.Destination
import timber.log.Timber

@Composable
@Destination
fun MyLibraryScreen(
    myLibraryScreenNavigator: MyLibraryScreenNavigator,
    myLibraryViewModel: MyLibraryViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val myBooksList = myLibraryViewModel.booksWithProgress.collectAsStateWithLifecycle()
        val suggestedBooksList = myLibraryViewModel.suggestedBooksList.collectAsStateWithLifecycle()
        val readingList = myLibraryViewModel.readingLists.collectAsStateWithLifecycle()
        /*val uiEvents = myLibraryViewModel.uiEvents.collectAsStateWithLifecycle(
            initialValue = MyLibraryViewModel.MyLibraryScreenUiEvents.DefaultState
        )*/

        var shouldShowCreateReadingListDialog by rememberSaveable {
            mutableStateOf(false)
        }

       /* LaunchedEffect(key1 = true) {
            when (uiEvents.value) {
                is MyLibraryViewModel.MyLibraryScreenUiEvents.DefaultState -> {}
                is MyLibraryViewModel.MyLibraryScreenUiEvents.DismissReadListDialog -> {
                    //shouldShowCreateReadingListDialog = false
                }

                else -> {}
            }
        }*/

        if (shouldShowCreateReadingListDialog) {
            CreateReadingListDialog(
                onDismissRequest = {
                    shouldShowCreateReadingListDialog = false
                },
                onSaveReadingList = { image, name, description ->
                    myLibraryViewModel.createReadingList(
                        image = image,
                        name = name,
                        description = description
                    )
                    shouldShowCreateReadingListDialog = false
                },
                onAddImage = {
                    //open image selector ui
                }
            )
        }

        if (myLibraryViewModel.isLoading) {
            CircularProgressIndicator(
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            //show our screen
            MyLibraryStatelessScreen(
                booksList = myBooksList.value,
                suggestedBooksList = suggestedBooksList.value,
                readingLists = readingList.value,
                isFetchingSuggestions = myLibraryViewModel.isFetchingSuggestions,
                suggestionsErrorMessage = myLibraryViewModel.suggestionsErrorMessage,
                retryNetCall = {
                    myLibraryViewModel.getSuggestedBooks()
                },
                onAddMoreBookClicked = {
                    myLibraryScreenNavigator.openAddBookScreenDestination()
                },
                onSeeAllBookSelected = { bookId ->
                    myLibraryScreenNavigator.openBookTrackingScreenDestination(
                        bookId,
                        PreviousScreenDestinations.LIBRARY_SCREEN
                    )
                },
                onCreateReadingList = {
                    shouldShowCreateReadingListDialog = true
                } ,
                onDeleteList = {
                    myLibraryViewModel.deleteReadingList(it)
                } ,
                onListSelected = {
                    //navigate to reading list screen
                }
            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun MyLibraryStatelessScreen(
    modifier: Modifier = Modifier,
    booksList: List<LibraryBookData> = listOf(LibraryBookData()),
    suggestedBooksList: List<SuggestedBook> = listOf(SuggestedBook()),
    readingLists: List<ReadingLists> = listOf(ReadingLists()),
    isFetchingSuggestions: Boolean = false,
    suggestionsErrorMessage: String = "",
    retryNetCall: () -> Unit = {},
    onCurrentlyReadingSeeAll: () -> Unit = {},
    onSeeAllBookSelected: (String) -> Unit = {},
    onAddMoreBookClicked: () -> Unit = {},
    onCreateReadingList: () -> Unit = {},
    onDeleteList: (String) -> Unit = {},
    onListSelected: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 8.dp, start = 8.dp)
            .verticalScroll(scrollState, true),
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentlyReadingSection(
            modifier = Modifier.fillMaxWidth(),
            bookList = booksList,
            onSeeAllClicked = {
                onCurrentlyReadingSeeAll()
            },
            onBookSelected = { id ->
                onSeeAllBookSelected(id)
            },
            onAddMoreClicked = {
                onAddMoreBookClicked()
            }
        )

        SuggestedForYouSection(
            bookList = suggestedBooksList,
            hasError = suggestionsErrorMessage.isNotEmpty(),
            isLoading = isFetchingSuggestions,
            errorMessage = suggestionsErrorMessage,
            onSeeAllClicked = {
                //if we have a lot of book suggestions send user to the screen
            },
            onAddToWishlist = {
                //add to wishlist of reading
            },
            retry = {
                retryNetCall()
            }
        )

        BookListSection(
            readingList = readingLists,
            onCreateNewReadinglist = {
                onCreateReadingList()
            },
            onListSelected = {
                //open reading list screen
                onListSelected(it)
            },
            onDeleteList = {
                //delete list form db
                onDeleteList(it)
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun CurrentlyReadingSection(
    modifier: Modifier = Modifier,
    bookList: List<LibraryBookData> = listOf(LibraryBookData()),
    onSeeAllClicked: () -> Unit = {},
    onBookSelected: (String) -> Unit = {},
    onAddMoreClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier.height(254.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.wrapContentWidth(),
                text = "My books",
                style = BookAppTypography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = { onSeeAllClicked() }
            ) {
                Text(
                    text = "see more",
                    style = BookAppTypography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = "")
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = if (bookList.isEmpty()) Arrangement.Center else Arrangement.spacedBy(
                12.dp
            )
        ) {
            if (bookList.isEmpty()) {
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

                items(items = bookList) { book ->
                    LibraryBookCardComponent(
                        book = book,
                        onItemSelected = {
                            onBookSelected(book.id)
                        }
                    )
                }

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

@Composable
fun SuggestedForYouSection(
    modifier: Modifier = Modifier,
    bookList: List<SuggestedBook> = listOf(SuggestedBook()),
    hasError: Boolean = false,
    errorMessage: String = "",
    isLoading: Boolean = false,
    retry: () -> Unit = {},
    onSeeAllClicked: () -> Unit = {},
    onAddToWishlist: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.height(170.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier.wrapContentWidth(),
                text = "Suggested for you",
                style = BookAppTypography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            /* TextButton(
                 onClick = { onSeeAllClicked() }
             ) {
                 Text(
                     text = "see more",
                     style = BookAppTypography.labelSmall,
                     color = MaterialTheme.colorScheme.secondary
                 )
                 Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = "")
             }*/
        }

        val colorsList = listOf(
            0xFF34E363,
            0xFFFCFF3B,
            0xFFff7d45,
            0xFF45e0ff,
            0xFF4f52ff,
            0xFFf25bfc,
            0xFFfa4b5a,

            )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(40.dp)
                            .fillMaxHeight(),
                        color = MaterialTheme.colorScheme.secondary,
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                }
            } else {
                if (hasError) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                6.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            Image(
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(70.dp),
                                painter = painterResource(id = R.drawable.ic_no_internet),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(Color.Gray)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = errorMessage,
                                textAlign = TextAlign.Center,
                                style = BookAppTypography.labelMedium
                            )
                            Button(
                                modifier = Modifier.width(100.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                ),
                                onClick = { retry() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    modifier = Modifier.padding(start = 3.dp),
                                    text = "Retry",
                                    style = BookAppTypography.labelMedium,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
                if (bookList.isEmpty()) {

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                6.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            /* Image(
                                 modifier = Modifier
                                     .width(70.dp)
                                     .height(70.dp) ,
                                 painter = painterResource(id = R.drawable.ic_no_internet),
                                 contentDescription = "" ,
                                 colorFilter = ColorFilter.tint(Color.Gray)
                             )*/
                            Text(
                                modifier = Modifier.width(250.dp),
                                text = "Oops! We could not get any suggestions at the moment",
                                textAlign = TextAlign.Center,
                                style = BookAppTypography.labelMedium,
                                maxLines = 2
                            )
                            Button(
                                modifier = Modifier.width(110.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                ),
                                onClick = { retry() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    modifier = Modifier.padding(start = 3.dp),
                                    text = "refresh",
                                    style = BookAppTypography.labelMedium,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }


                } else {
                    items(items = bookList) { book ->
                        if (book.id.isNotEmpty()) {
                            SuggestedBookCardComponent(
                                cardColor = colorsList.random(),
                                book = book,
                                onAddToWishlistSelected = { bookDets ->
                                    onAddToWishlist(bookDets.id)
                                }
                            )
                        }
                    }
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun BookListSection(
    readingList: List<ReadingLists> = listOf<ReadingLists>(),
    onFilterList: () -> Unit = {},
    onCreateNewReadinglist: () -> Unit = {},
    onDeleteList: (String) -> Unit = {},
    onListSelected: (String) -> Unit = {}
) {
    Timber.tag("BookListSection").d("reading list : ${readingList.toString()}")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.Top
        )

    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.weight(0.5f),
                text = "My reading lists",
                style = BookAppTypography.labelLarge
            )

            TextButton(
                onClick = { onCreateNewReadinglist() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add icon",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "create",
                    style = BookAppTypography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

            }
            /*
                        TextButton(
                            onClick = { onFilterList() }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "add icon",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(text = "create" , style = BookAppTypography.labelSmall)

                        }*/
        }


        val state = rememberLazyListState()

        LaunchedEffect(readingList) {
            snapshotFlow { state.firstVisibleItemIndex }
                .collect {
                    // Scroll to the top if a new item is added.
                    // (But only if user is scrolled to the top already.)
                    if (it <= 1) {
                        state.scrollToItem(0)
                    }
                }
        }

        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            contentPadding = PaddingValues(bottom = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (readingList.isEmpty()) {

                item {

                    Text(
                        modifier = Modifier.width(250.dp),
                        text = "No reading lists currently available. Please click on the add icon to create a new one.",
                        textAlign = TextAlign.Center,
                        style = BookAppTypography.labelMedium,
                        maxLines = 2
                    )

                }

            } else {

                items(readingList, key = {
                    it.id
                }) {
                    val dismissState = rememberDismissState()

                    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                        //perform delete action here
                        onDeleteList(it.id)
                    }
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.StartToEnd),
                        background = {
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> MaterialTheme.colorScheme.background
                                    else -> Color.Red
                                }, label = ""
                            )
                            val alignment = Alignment.CenterStart
                            val icon = Icons.Default.Delete

                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                                label = ""
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = Dp(20f)),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Delete Icon",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {
                            ReadingListItem(
                                readingLists = it,
                                onListSelected = { listId ->
                                    //open reading list screen
                                    onListSelected(listId)
                                }
                            )
                        }
                    )

                }
            }

        }
    }

}

@Composable
@Preview(showBackground = true)
fun ReadingListItem(
    modifier: Modifier = Modifier,
    readingLists: ReadingLists = ReadingLists(),
    onListSelected: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .clickable {
                onListSelected(readingLists.id)
            }
    ) {
        CoilImageComponent(
            image = readingLists.image,
            modifier = Modifier
                .width(57.dp)
                .height(61.dp)
        )

        Column(
            modifier = Modifier.weight(0.5f)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = readingLists.name,
                style = BookAppTypography.labelMedium,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "0/${readingLists.readingList.size} books read",
                style = BookAppTypography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = readingLists.date,
                style = BookAppTypography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                modifier = Modifier.width(250.dp),
                // trackColor = Color.Gray ,
                strokeCap = StrokeCap.Round,
                progress = 0.5f
            )

        }
    }

}

@Composable
@Preview()
fun CreateReadingListDialog(
    imageSelectorUiState: ImageSelectorUiState = ImageSelectorUiState(),
    onSaveReadingList: (String, String, String) -> Unit = { _, _, _ -> },
    onAddImage: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {

    var nameText by rememberSaveable {
        mutableStateOf("")
    }
    var nameFieldError by rememberSaveable {
        mutableStateOf(false)
    }
    var descriptionText by rememberSaveable {
        mutableStateOf("")
    }
    var descriptionFieldError by rememberSaveable {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(4.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.onBackground
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = R.drawable.ic_playlist_add),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                }
                //Spacer(modifier = Modifier.height(6.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "create new reading list",
                    style = BookAppTypography.labelLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))

                ImageSelectorComponent(
                    imageSelectorState = ImageSelectorUiState(),
                    width = 260.dp ,
                    height = 140.dp ,
                   /* isCircular = true,
                    size = 100.dp,*/
                    onSelect = {
                        onAddImage()
                    },
                    onClear = {

                    }
                )

                OutlinedTextFieldComponent(
                    modifier = Modifier.fillMaxWidth(),
                    hint = "name",
                    text = nameText,
                    onTextChanged = {
                        nameText = it
                    },
                    isSingleLine = true,
                    hasError = nameFieldError,
                    roundedCornerSize = 5.dp
                )

                OutlinedTextFieldComponent(
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    hint = "description",
                    text = descriptionText,
                    onTextChanged = {
                        descriptionText = it
                    },
                    isSingleLine = false,
                    hasError = descriptionFieldError,
                    roundedCornerSize = 5.dp ,
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    // .padding(5.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    OutlinedButton(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = { onDismissRequest() }
                        /*colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )*/
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(text = "cancel", style = BookAppTypography.labelMedium)
                    }

                    Button(
                        modifier = Modifier.wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            /*save reading list*/
                            if (nameText.isBlank()) {
                                nameFieldError = true
                            } else if (descriptionText.isBlank()) {
                                descriptionFieldError = true
                            } else {
                                onSaveReadingList(
                                    imageSelectorUiState.imageSelectedUri.toString(),
                                    nameText,
                                    descriptionText
                                )
                            }
                        })
                    {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.background
                        )
                        Text(text = "create list", style = BookAppTypography.labelMedium)
                    }
                }


            }

        }


    }

}

