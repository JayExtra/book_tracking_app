package com.dev.james.booktracker.compose_ui.ui.components

import android.content.Context
import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.AddBookViewModel
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.GoogleBottomSheetUiState
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.utilities.convertToAuthorsString
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import timber.log.Timber

@Composable
fun GoogleBooksSearchBottomSheet(
    addBookViewModel : AddBookViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val searchFieldState by remember { mutableStateOf(addBookViewModel.bottomSheetSearchFieldState) }

    val googleSearchBottomSheetUiState: GoogleBottomSheetUiState by addBookViewModel.googleBottomSheetSearchState
        .collectAsStateWithLifecycle()

   StateLessGoogleBooksSearchBottomSheet(
        searchFieldState = searchFieldState,
        googleSearchBottomSheetUiState = googleSearchBottomSheetUiState,
        context = context,
        onBookSelected = { book ->
            // update the view model image state and also pass the book selected data to various required fields
            addBookViewModel.onBookSelected(book = book)

        },
        onSearchTextChanged = { query ->
            // update the search query
            addBookViewModel.searchForBook(query)
        }
    )

}

@Composable
@Preview("GoogleSearchBottomSheet", showBackground = true)
fun StateLessGoogleBooksSearchBottomSheet(
    context: Context = LocalContext.current,
    searchFieldState: FormState<TextFieldState> = FormState(fields = listOf()),
    googleSearchBottomSheetUiState: GoogleBottomSheetUiState = GoogleBottomSheetUiState.IsLoading,
    onSearchTextChanged: (String) -> Unit = {},
    onBookSelected: (Book) -> Unit = {}
) {
    val searchState: TextFieldState = searchFieldState.getState("search_field")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            text = searchState.value,
            hint = "Search for any book",
            isSingleLine = true,
            startingIcon = Icons.Default.Search,
            trailingIcon = Icons.Default.Close,
            onTrailingIconClicked = {
                //update the text field state to empty
                searchState.change("")
            },
            onTextChanged = { query ->
                searchState.change(query)
                onSearchTextChanged(query)
            }
        )



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {

            when (googleSearchBottomSheetUiState) {
                is GoogleBottomSheetUiState.StandbyState -> {

                    AnimationWithMessageComponent(
                        animation = LottieCompositionSpec.RawRes(R.raw.search_lottie) ,
                        shouldShow = true ,
                        message = "Search for any books online."
                    )

                }
                is GoogleBottomSheetUiState.IsLoading -> {
                    CircularProgressIndicator(
                        strokeWidth = 3.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                is GoogleBottomSheetUiState.HasFetched -> {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        // contentPadding = PaddingValues(8.dp),
                        content = {
                            val booksList = googleSearchBottomSheetUiState.booksList
                            if (booksList.isNotEmpty()) {
                                items(booksList ,
                                    key = {
                                      book -> book.bookId!!
                                    }){ book ->
                                    BookInformationCard(
                                        book,
                                        onBookSelected = {
                                            // on book selected
                                            onBookSelected(book)
                                        }
                                    )
                                }
                            }else {
                                item {
                                    EmptyAnimationSection(
                                        animation = LottieCompositionSpec.RawRes(R.raw.empty_search_lottie) ,
                                        shouldShow = true,
                                        message = "No results found."
                                    )
                                }
                            }
                        }
                    )
                }

                is GoogleBottomSheetUiState.HasFailed -> {
                    Timber.tag("GoogleBottomSheet")
                        .d("Failed to fetch books , reason: ${googleSearchBottomSheetUiState.errorMessage}")
                    val error = googleSearchBottomSheetUiState.errorMessage
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally ,
                        verticalArrangement = Arrangement.Center ,
                        modifier = Modifier.padding(8.dp)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.baseline_wifi_off_24),
                            contentDescription = "No network icon" ,
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp) ,
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = error , style = BookAppTypography.bodyMedium , textAlign = TextAlign.Center)

                    }
                }
            }

        }
    }

}

@Composable
fun BookInformationCard(
    book: Book,
    onBookSelected: () -> Unit = {},
) {

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onBookSelected()
            }
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest
                .Builder(LocalContext.current)
                .data(data = book.bookImage)
                .apply(block = {
                    //placeholder(R.drawable.image_placeholder_24)
                    error(R.drawable.image_placeholder_24)
                    crossfade(true)
                    transformations(
                        RoundedCornersTransformation(0f)
                    )
                }).build()
        )

        val painterState = painter.state

        Box(modifier = Modifier
            .height(150.dp)
            .width(110.dp)
            .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "book thumbnail" ,
                contentScale = ContentScale.Fit
            )
            if(painterState is AsyncImagePainter.State.Loading){
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }


        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book.bookTitle ?: "No title found",
                style = BookAppTypography.headlineSmall ,
                maxLines = 2
            )

            Text(
                text = book.bookAuthors?.convertToAuthorsString() ?: "No authors found",
                style = BookAppTypography.bodySmall ,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            book.publisher?.let { publisher ->

                Text(
                    text = publisher,
                    style = BookAppTypography.bodySmall ,
                    maxLines = 2 ,
                    fontSize = 10.sp
                )
            }

            book.publishedDate?.let { date ->

                Text(
                    text = date,
                    style = BookAppTypography.bodySmall ,
                    maxLines = 2 ,
                    fontSize = 10.sp
                )
            }

        }
    }

}