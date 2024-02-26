package com.dev.james.my_library.presentation.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.BottomCardDecorationComponent
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
    myLibraryScreenNavigator: MyLibraryScreenNavigator,
    myLibraryViewModel: MyLibraryViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val myBooksList = myLibraryViewModel.booksWithProgress.collectAsStateWithLifecycle()

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
    onCurrentlyReadingSeeAll: () -> Unit = {},
    onSeeAllBookSelected: (String) -> Unit = {},
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
    cardColor: Long = DefaultColors.DEFAULT_CARD_COLOR
) {
/*    val bookDescText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp)
        ) {
            append("Some title")
        }
        append("\n")
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 8.sp)
        ) {
            append("Some author")
        }
        append("\n")
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 8.sp)
        ) {
            append("Some publisher , year")
        }
        append("\n")
        withStyle(
            style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 6.sp)
        ) {
            append("200 pgs")
        }
    }*/

    /*   val bookDescText = """
           Some title
           Some author
           Some published , year
           200pgs
       """.trimIndent()
   */


    Card(
        modifier = Modifier
            .width(222.dp)
            .height(129.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(cardColor)
        ),
        onClick = {}
    ) {
        val constraintsSet = ConstraintSet {
            val bookImage = createRefFor("book_image")
            val bookDetails = createRefFor("book_details")
            val wishlistBtn = createRefFor("wishlist_button")


            constrain(bookImage) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }

            constrain(bookDetails) {
                start.linkTo(bookImage.end, 4.dp)
                top.linkTo(bookImage.top)
            }

            constrain(wishlistBtn) {
                top.linkTo(bookDetails.bottom , 4.dp)
                start.linkTo(bookImage.end, 4.dp)

            }
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .drawBehind {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val arcRadius = 120.dp
                    val arcRadiusSmall = 85.dp
                    val circleRadius = 25.dp

                    drawArc(
                        size = Size(width = 120f, height = 120f),
                        color = Color(DefaultColors.CARD_DECORATOR_DEFAULT_COLOR),
                        useCenter = false,
                        startAngle = 90f,
                        sweepAngle = 180f,
                        style = Stroke(7.dp.toPx()),
                        topLeft = Offset(
                            canvasWidth / 2 + arcRadius.toPx() / 2,
                            canvasHeight / 2 + arcRadius.toPx() / 8
                        )
                    )

                    drawCircle(
                        color = Color(DefaultColors.CARD_DECORATOR_DEFAULT_COLOR),
                        radius = 25f,
                        center = Offset(
                            canvasWidth - circleRadius.toPx() / 1.1f,
                            canvasHeight - circleRadius.toPx() / 1.4f
                        )
                    )

                    drawArc(
                        size = Size(width = 85f, height = 85f),
                        color = Color(DefaultColors.CARD_DECORATOR_DEFAULT_COLOR),
                        useCenter = false,
                        startAngle = -90f,
                        sweepAngle = 180f,
                        style = Stroke(5.dp.toPx()),
                        topLeft = Offset(
                            canvasWidth - arcRadiusSmall.toPx() / 2.5f,
                            canvasHeight - arcRadiusSmall.toPx() / 2.5f
                        )
                    )
                },
            constraintSet = constraintsSet
        ) {

            CoilImageComponent(
                modifier = Modifier
                    .layoutId("book_image")
                    .width(48.dp)
                    .height(75.dp)
            )
            BookDescriptionTextSection(
                modifier = Modifier
                    .layoutId("book_details")
                    .width(135.dp)
                    .wrapContentHeight()
            )

            Button(
                modifier = Modifier
                    .layoutId("wishlist_button")
                    .width(66.dp)
                    .height(20.dp),
                onClick = { /*TODO*/ } ,
                contentPadding = PaddingValues(2.dp)
            ) {

                Icon(
                    modifier = Modifier
                        .width(9.dp)
                        .height(18.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
                Text(
                    text = "wishlist" ,
                    style = BookAppTypography.labelSmall ,
                    fontSize = 8.sp
                )
            }

        }

    }
}


@Composable
fun BookDescriptionTextSection(
    modifier : Modifier = Modifier
){
    Column(
        modifier = modifier ,
        verticalArrangement = Arrangement.spacedBy(2.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = "Some title" ,
            style = BookAppTypography.labelMedium ,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp ,
            maxLines = 2 ,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = "Author name" ,
            style = BookAppTypography.bodyMedium ,
            fontWeight = FontWeight.Medium,
            fontSize = 8.sp ,
            maxLines = 1 ,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Publisher name , date" ,
            style = BookAppTypography.labelMedium ,
            fontWeight = FontWeight.Light,
            fontSize = 8.sp ,
            maxLines = 1 ,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = "Pages Count",
            style = BookAppTypography.labelSmall ,
            fontWeight = FontWeight.Bold,
            fontSize = 6.sp
        )
    }
}
