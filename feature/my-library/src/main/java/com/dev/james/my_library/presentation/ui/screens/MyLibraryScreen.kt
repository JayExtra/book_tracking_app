package com.dev.james.my_library.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.components.LibraryBookCardComponent
import com.dev.james.booktracker.compose_ui.ui.enums.DefaultColors
import com.dev.james.booktracker.compose_ui.ui.enums.PreviousScreenDestinations
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.my_library.presentation.navigation.MyLibraryScreenNavigator
import com.dev.james.my_library.presentation.ui.viewmodel.MyLibraryViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun MyLibraryScreen(
    myLibraryScreenNavigator: MyLibraryScreenNavigator ,
    myLibraryViewModel: MyLibraryViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val myBooksList = myLibraryViewModel.booksWithProgress.collectAsStateWithLifecycle()

        if(myLibraryViewModel.isLoading){
            CircularProgressIndicator(
                strokeWidth = 4.dp ,
                strokeCap = StrokeCap.Round ,
                color = MaterialTheme.colorScheme.secondary
            )
        }else{
         //show our screen
            MyLibraryStatelessScreen(
                booksList = myBooksList.value ,
                onAddMoreBookClicked = {
                    myLibraryScreenNavigator.openAddBookScreenDestination()
                } ,
                onSeeAllBookSelected = { bookId ->
                    myLibraryScreenNavigator.openBookTrackingScreenDestination(bookId , PreviousScreenDestinations.LIBRARY_SCREEN)
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
    onCurrentlyReadingSeeAll: () -> Unit = {},
    onSeeAllBookSelected: (String) -> Unit = {} ,
    onAddMoreBookClicked: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState, true),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentlyReadingComponent(
            modifier = Modifier.fillMaxWidth() ,
            bookList = booksList ,
            onSeeAllClicked = {
                onCurrentlyReadingSeeAll()
            } ,
            onBookSelected = {id ->
                onSeeAllBookSelected(id)
            } ,
            onAddMoreClicked = {
                onAddMoreBookClicked()
            }
        )


    }
}

@Preview(showBackground = true)
@Composable
fun CurrentlyReadingComponent(
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SuggestionCardComponent(
    cardColor : Long = DefaultColors.DEFAULT_CARD_COLOR
){
    Card(
        modifier = Modifier
            .width(222.dp)
            .height(129.dp) ,
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) ,
        colors = CardDefaults.cardColors(
            containerColor = Color(cardColor)
        ),
        onClick = {}
    ) {
        val constraintsSet = ConstraintSet {
            val bookImage = createRefFor("book_image")
            val bookDetails = createRefFor("book_details")
            val wishlistBtn = createRefFor("wishlist_button")
            val decorator = createRefFor("decorator")

            constrain(bookImage){
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }

            constrain(bookDetails){
                start.linkTo(bookImage.end , 2.dp)
                top.linkTo(bookImage.top)
            }

            constrain(wishlistBtn){
                top.linkTo(bookDetails.bottom , margin = 8.dp)
                start.linkTo(bookDetails.end , 2.dp)

            }

            constrain(decorator){
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp) ,
            constraintSet = constraintsSet
        ) {

            CoilImageComponent(
                modifier = Modifier.layoutId("book_image")
                    .width(48.dp)
                    .height(75.dp)
            )




        }

    }

}
