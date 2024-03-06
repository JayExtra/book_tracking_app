package com.dev.james.my_library.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.dev.james.booktracker.compose_ui.R
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.LibraryBookCardComponent
import com.dev.james.booktracker.compose_ui.ui.enums.PreviousScreenDestinations
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.SuggestedBook
import com.dev.james.my_library.presentation.navigation.MyLibraryScreenNavigator
import com.dev.james.my_library.presentation.ui.components.SuggestedBookCardComponent
import com.dev.james.my_library.presentation.ui.viewmodel.MyLibraryViewModel
import com.ramcosta.composedestinations.annotation.Destination

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
    suggestedBooksList : List<SuggestedBook> = listOf(SuggestedBook()),
    isFetchingSuggestions : Boolean = false,
    suggestionsErrorMessage : String = "" ,
    retryNetCall : () -> Unit = {} ,
    onCurrentlyReadingSeeAll: () -> Unit = {},
    onSeeAllBookSelected: (String) -> Unit = {},
    onAddMoreBookClicked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 8.dp, start = 8.dp)
            .verticalScroll(scrollState, true),
        verticalArrangement = Arrangement.spacedBy(10.dp , alignment = Alignment.Top),
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
            bookList = suggestedBooksList ,
            hasError = suggestionsErrorMessage.isNotEmpty(),
            isLoading = isFetchingSuggestions,
            errorMessage = suggestionsErrorMessage,
            onSeeAllClicked = {
                //if we have a lot of book suggestions send user to the screen
            } ,
            onAddToWishlist = {
                //add to wishlist of reading
            } ,
            retry = {
                retryNetCall()
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
                text = "currently reading",
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
            horizontalArrangement = if(bookList.isEmpty()) Arrangement.Center else Arrangement.spacedBy(12.dp)
        ) {
            if(bookList.isEmpty()){
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally ,
                        verticalArrangement = Arrangement.spacedBy(space = 5.dp , alignment = Alignment.CenterVertically)
                    ) {
                        Text(
                            modifier = Modifier.width(200.dp) ,
                            text = "There are no books you are currently reading. Proceed to add one?" ,
                            style = BookAppTypography.bodyMedium ,
                            fontSize = 12.sp ,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            modifier = Modifier.wrapContentWidth() ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            onClick = { onAddMoreClicked() }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "" , tint = MaterialTheme.colorScheme.onBackground )

                            Text(modifier = Modifier.padding(start = 3.dp),text = "add a book" , style = BookAppTypography.labelMedium , fontSize = 10.sp)
                        }
                    }
                }

            }else {

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
    hasError : Boolean = false ,
    errorMessage : String = "" ,
    isLoading : Boolean = false ,
    retry : () -> Unit = {},
    onSeeAllClicked: () -> Unit = {},
    onAddToWishlist : (String) -> Unit = {}
){
    Column(
        modifier = modifier.height(254.dp),
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
            0xFF34E363 ,
            0xFFFCFF3B ,
            0xFFff7d45 ,
            0xFF45e0ff ,
            0xFF4f52ff ,
            0xFFf25bfc ,
            0xFFfa4b5a ,

        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp , Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if(isLoading){
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(40.dp)
                            .fillMaxHeight(),
                        color = MaterialTheme.colorScheme.secondary ,
                        strokeWidth = 5.dp ,
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            if(hasError){
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(8.dp) ,
                        horizontalAlignment = Alignment.CenterHorizontally ,
                        verticalArrangement = Arrangement.spacedBy(6.dp , Alignment.CenterVertically)
                    ) {
                        Image(
                            modifier = Modifier
                                .width(70.dp)
                                .height(70.dp) ,
                            painter = painterResource(id = R.drawable.ic_no_internet),
                            contentDescription = "" ,
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                        Text(modifier = Modifier.fillMaxWidth(), text = errorMessage , textAlign = TextAlign.Center , style = BookAppTypography.labelMedium)
                        Button(
                            modifier = Modifier.width(100.dp) ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            onClick = { retry() }
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "" , tint = MaterialTheme.colorScheme.onBackground )
                            Text(modifier = Modifier.padding(start = 3.dp),text = "Retry" , style = BookAppTypography.labelMedium , fontSize = 10.sp)
                        }
                    }
                }
            }else {
                items(items = bookList) { book ->
                    if (book.id.isNotEmpty()){
                        SuggestedBookCardComponent(
                            cardColor = colorsList.random() ,
                            book = book ,
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

