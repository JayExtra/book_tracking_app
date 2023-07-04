package com.dev.james.booktracker.home.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleBooksSearchBottomSheet(
    readGoalsScreenViewModel : ReadGoalsScreenViewModel = hiltViewModel()
){

    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(

    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateLessGoogleBooksSearchBottomSheet(
    coroutineScope : CoroutineScope ,
    modalSheetState : SheetState ,
    onSearchTextChanged : (String) -> Unit = {}  ,
    onBookSelected : (Book) -> Unit = {}
){
    ModalBottomSheet(
        onDismissRequest = { /*TODO*/ }
    ) {

    }

}