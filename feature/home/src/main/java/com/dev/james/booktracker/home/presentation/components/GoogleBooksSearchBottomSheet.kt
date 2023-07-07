package com.dev.james.booktracker.home.presentation.components

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenState
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

@Composable
fun GoogleBooksSearchBottomSheet(
    readGoalsScreenViewModel: ReadGoalsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val searchFieldState by remember { mutableStateOf(readGoalsScreenViewModel.bottomSheetSearchFieldState) }

    val googleSearchBottomSheetUiState : ReadGoalsScreenViewModel.GoogleBottomSheetUiState by readGoalsScreenViewModel.googleBottomSheetSearchState
        .collectAsStateWithLifecycle()

    StateLessGoogleBooksSearchBottomSheet(
        searchFieldState = searchFieldState,
        googleSearchBottomSheetUiState = googleSearchBottomSheetUiState ,
        context = context ,
        onBookSelected = { book ->
            // update the view model
            Toast.makeText(context , "book seletced $book" , Toast.LENGTH_SHORT ).show()
        },
        onSearchTextChanged = { query ->
            // update the search query
            readGoalsScreenViewModel.searchForBook(query)
        }
    )

}

@Composable
@Preview("GoogleSearchBottomSheet", showBackground = true)
fun StateLessGoogleBooksSearchBottomSheet(
    context : Context = LocalContext.current,
    searchFieldState: FormState<TextFieldState> = FormState(fields = listOf()),
    googleSearchBottomSheetUiState: ReadGoalsScreenViewModel.GoogleBottomSheetUiState = ReadGoalsScreenViewModel.GoogleBottomSheetUiState.IsLoading,
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
        TextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            text = searchState.value,
            hint = "Search for any book",
            startingIcon = Icons.Default.Search,
            trailingIcon = Icons.Default.Close,
            onClearTextField = {
                //update the text field state to empty
                searchState.change("")
            },
            onTextChanged = { query ->
                searchState.change(query)
                onSearchTextChanged(query)
            }
        )



        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            when (googleSearchBottomSheetUiState) {
                is ReadGoalsScreenViewModel.GoogleBottomSheetUiState.IsLoading -> {
                    CircularProgressIndicator(
                        strokeWidth = 3.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                is ReadGoalsScreenViewModel.GoogleBottomSheetUiState.HasFetched -> {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentPadding = PaddingValues(8.dp),
                        content = {
                            val booksList = googleSearchBottomSheetUiState.booksList
                            if (booksList.isNotEmpty()) {
                                items(booksList) { book ->
                                    BookInformationCard(
                                        book,
                                        onBookSelected = {
                                            // on book selected
                                            onBookSelected(book)
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
                is ReadGoalsScreenViewModel.GoogleBottomSheetUiState.HasFailed -> {
                    Timber.tag("GoogleBottomSheet").d("Failed to fetch books , reason: ${googleSearchBottomSheetUiState.errorMessage}")
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
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
        Image(
            rememberAsyncImagePainter(book.bookThumbnail),
            contentDescription = "book thumbnail",
            modifier = Modifier
                .height(150.dp)
                .width(120.dp)
                .clip(shape = RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = book.bookTitle,
                style = BookAppTypography.headlineMedium
            )

            Text(
                text = book.bookAuthors.toString(),
                style = BookAppTypography.bodySmall
            )
        }
    }

}