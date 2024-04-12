package com.dev.james.booktracker.compose_ui.ui.states

import com.dev.james.booktracker.core.common_models.Book

sealed class GoogleBottomSheetUiState {
    object IsLoading : GoogleBottomSheetUiState()

    object StandbyState : GoogleBottomSheetUiState()

    data class HasFetched(val booksList: List<Book>) : GoogleBottomSheetUiState()

    data class HasFailed(val errorMessage: String) : GoogleBottomSheetUiState()

}