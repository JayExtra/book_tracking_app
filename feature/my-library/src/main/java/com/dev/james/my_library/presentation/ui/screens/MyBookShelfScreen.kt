package com.dev.james.my_library.presentation.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dev.james.booktracker.compose_ui.ui.components.GoogleBooksSearchBottomSheet
import com.dev.james.booktracker.compose_ui.ui.components.PortraitBooksRowComponent
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.states.GoogleBottomSheetUiState
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.my_library.presentation.ui.viewmodel.BookShelfUIState
import com.dev.james.my_library.presentation.ui.viewmodel.MyBookShelfViewModel
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MyBookShelfScreen(
    viewModel : MyBookShelfViewModel = hiltViewModel(),
){
    val state = viewModel.screenState.collectAsStateWithLifecycle()
    MyBookShelfStatelessScreen(
        state = state.value ,
        onAction = { action ->
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun MyBookShelfStatelessScreen(
    state : BookShelfUIState = BookShelfUIState(),
    searchFieldState: FormState<TextFieldState> = FormState(fields = listOf()),
    googleBottomSheetUiState: GoogleBottomSheetUiState = GoogleBottomSheetUiState.StandbyState,
    onAction : (MyBookShelfViewModel.BookShelfUIActions) -> Unit = {}
){
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
                    Text(text = "My Book Shelf", style = BookAppTypography.headlineMedium)
                },
                navigate = {

                },

                showBackArrow = true,
               /* navActions = {
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
                }*/
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

                    Text(text = "Search for your book", style = BookAppTypography.headlineSmall , textAlign = TextAlign.Start)
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

                GoogleBooksSearchBottomSheet(
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
                )
            }
        } ,
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)){
                if(state.isLoading){
                    CircularProgressIndicator(
                        strokeCap = StrokeCap.Round ,
                        strokeWidth = 3.dp
                    )
                }else {
                    Column(
                        modifier = Modifier.fillMaxSize() ,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        val sections = listOf("Currently Reading" , "Not Started" , "Completed" , "My Wish List")
                        for(section in sections){
                            PortraitBooksRowComponent(
                                sectionTitle = section ,
                                items = listOf(BaseBookData)
                            ) {

                            }
                        }
                    }
                }
            }
        }
    )


}

@Preview
@Composable
private fun MyBookShelfScreenPreview(){
    BookTrackerTheme{
        MyBookShelfStatelessScreen(
            state = BookShelfUIState() ,
            onAction = {}
        )
    }
}