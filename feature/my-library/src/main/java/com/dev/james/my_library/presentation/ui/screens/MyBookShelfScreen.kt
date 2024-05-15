package com.dev.james.my_library.presentation.ui.screens

import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dev.james.booktracker.compose_ui.ui.theme.BookTrackerTheme
import com.dev.james.my_library.presentation.ui.viewmodel.BookShelfUIState
import com.dev.james.my_library.presentation.ui.viewmodel.MyBookShelfViewModel

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

@Composable
private fun MyBookShelfStatelessScreen(
    state : BookShelfUIState = BookShelfUIState(),
    onAction : (MyBookShelfViewModel.BookShelfUIActions) -> Unit = {}
){

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