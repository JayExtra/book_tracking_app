package com.dev.james.my_library.presentation.ui.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.booktracker.core.common_models.mappers.mapToLibraryData
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository
import com.dev.james.domain.usecases.FetchActiveBookProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReadingListScreenViewModel @Inject constructor(
    private val readingListsRepository: ReadingListsRepository,
    private val fetchActiveBookProgress: FetchActiveBookProgress ,
    private val booksRepository: BooksRepository
) : ViewModel() {

    companion object {
        const val TAG = "ReadingListScreenViewModel"
    }

    private val _state: MutableStateFlow<ReadingListScreenState> =
        MutableStateFlow(value = ReadingListScreenState())
    val state get() = _state.asStateFlow()

    var deleteConfirmationDialogState by mutableStateOf(value = false)
        private set
    var deleteReadingListDialogState by mutableStateOf(false)
        private set

    var deleteBookConfirmationDialogState by mutableStateOf(false)
        private set

    var bookDeletedDialogState by mutableStateOf(false)
        private set

    /*var selectedBookState by mutableStateOf(LibraryBookData())
        private set*/

    @SuppressLint("NewApi")
    fun onAction(action: ReadingListScreenAction) = viewModelScope.launch {
        when (action) {
            is ReadingListScreenAction.FetchReadingList -> {
                _state.update { screenState ->
                    screenState.copy(
                        isLoading = true,
                        readingListFetchError = "" ,
                        readingList = ReadingLists()
                    )
                }
                fetchReadingListDetails(
                    action.readingListId
                )
            }
            is ReadingListScreenAction.DeleteReadingList -> {
                readingListsRepository.deleteReadingList(action.readingListId)
                val readingListIds = readingListsRepository.getReadingLists().first().map { it.id }
                if(!readingListIds.contains(action.readingListId)){
                    deleteReadingListDialogState = false
                    deleteConfirmationDialogState = true
                }
            }
            is ReadingListScreenAction.DeleteBook -> {
               /* selectedBookState = action.book*/
                val result = booksRepository.addOrRemoveBookFromReadingList(
                    bookId = action.book.id ,
                    readingListId = action.readingListId
                )
                when(result){
                    is Resource.Success -> {
                        val message = result.data?.split(":")!!
                        if(message[0] == "removed"){
                            bookDeletedDialogState = true
                            deleteBookConfirmationDialogState = false
                        }
                    }
                    is Resource.Error -> {
                        Timber.tag(TAG).d("ERROR : ${result.message}")
                    }
                    else -> {}
                }
            }
            is ReadingListScreenAction.DismissDialog -> {
                when(action.dialogType){
                     DialogType.READING_LIST_DELETED_DIALOG -> {
                        deleteConfirmationDialogState = false
                     }
                    DialogType.CONFIRM_DELETE_READING_LIST_DIALOG -> {
                        deleteReadingListDialogState = false
                    }
                    DialogType.BOOK_DELETED_DIALOG -> {
                        bookDeletedDialogState = false
                    }
                    DialogType.CONFIRM_BOOK_DELETE_DIALOG -> {
                        deleteBookConfirmationDialogState = false
                    }
                }
            }

            is ReadingListScreenAction.ShowDialog -> {
                when(action.dialogType){
                    DialogType.CONFIRM_DELETE_READING_LIST_DIALOG -> {
                        deleteReadingListDialogState = true
                    }
                    DialogType.CONFIRM_BOOK_DELETE_DIALOG -> {
                        deleteBookConfirmationDialogState = true
                    }
                    else -> {}
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchReadingListDetails(id: String) = viewModelScope.launch {
        readingListsRepository.getSingleReadingList(id)
            .map { item ->
                val differedBooks = item.readingList.map {
                    async { fetchActiveBookProgress.invoke(it) }
                }
                val booksListWithData = differedBooks.awaitAll().map {
                    it.mapToLibraryData()
                }
                Timber.tag(TAG).e("books with data : $booksListWithData")
                ReadingLists(
                    id = item.id,
                    name = item.name,
                    image = item.image,
                    description = item.description,
                    books = booksListWithData,
                    date = item.date,
                    starred = item.starred
                )
            }.catch { t ->
                Timber.tag(TAG).e("Error fetching reading list => ${t.printStackTrace()}")
                _state.update { screenState ->
                    screenState.copy(
                        isLoading = false,
                        readingList = ReadingLists(),
                        readingListFetchError = "Could not fetch reading list details => ${t.localizedMessage}"
                    )
                }
            }
            .collectLatest { readingList ->
                _state.update { screenState ->
                    screenState.copy(
                        isLoading = false,
                        readingList = readingList,
                        readingListFetchError = ""
                    )
                }
            }
    }


    sealed class ReadingListScreenAction {
        data class FetchReadingList(val readingListId: String) : ReadingListScreenAction()
        data class DeleteReadingList(val readingListId :String) : ReadingListScreenAction()

        data class DeleteBook(val book : LibraryBookData , val readingListId: String) : ReadingListScreenAction()

        data class DismissDialog(val dialogType : DialogType) : ReadingListScreenAction()

        data class ShowDialog(val dialogType: DialogType) : ReadingListScreenAction()
    }

    data class ReadingListScreenState(
        val isLoading: Boolean = false,
        val readingList: ReadingLists = ReadingLists(),
        val readingListFetchError: String = ""
    )

    enum class DialogType {
        READING_LIST_DELETED_DIALOG ,
        BOOK_DELETED_DIALOG ,
        CONFIRM_DELETE_READING_LIST_DIALOG ,
        CONFIRM_BOOK_DELETE_DIALOG
    }
}
