package com.dev.james.my_library.presentation.ui.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.ReadingListItem
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.booktracker.core.common_models.SuggestedBook
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.utilities.generateSecureUUID
import com.dev.james.booktracker.core.utilities.getCurrentDateAndTime
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository
import com.dev.james.domain.usecases.FetchSuggestedBooksUsecase
import com.dev.james.domain.usecases.GetBooksAndProgressUsecase
import com.dev.james.domain.usecases.ReadingListUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject



@HiltViewModel
class MyLibraryViewModel @Inject constructor(
    private val getBooksAndProgressUsecase: GetBooksAndProgressUsecase ,
    private val fetchSuggestedBooksUsecase: FetchSuggestedBooksUsecase ,
    private val readingListUsecase: ReadingListUsecase ,
    private val readingListsRepository: ReadingListsRepository
) : ViewModel() {

    companion object {
        const val TAG = "MyLibraryViewModel"
    }

    private var _booksWithProgress : MutableStateFlow<List<LibraryBookData>> = MutableStateFlow(value = listOf(LibraryBookData()))
    val booksWithProgress get() = _booksWithProgress.asStateFlow()

    private var _suggestedBooksList : MutableStateFlow<List<SuggestedBook>> = MutableStateFlow(value = listOf(
        SuggestedBook()
    ))
    val suggestedBooksList get() = _suggestedBooksList.asStateFlow()

    var isLoading by mutableStateOf(value = false)
        private set
    var suggestionsErrorMessage by mutableStateOf(value = "")
        private set

    var isFetchingSuggestions by mutableStateOf(value = false)
        private set

    private var _readingLists : MutableStateFlow<List<ReadingLists>> = MutableStateFlow(listOf(
        ReadingLists()
    ))
    val readingLists get() = _readingLists.asStateFlow()

    private val _uiEvents : Channel<MyLibraryScreenUiEvents> = Channel<MyLibraryScreenUiEvents>()
    val uiEvents get() = _uiEvents.receiveAsFlow()

    init {
        isLoading = true
        getBooksWithProgress()
        getSuggestedBooks()
        fetchReadingList()
    }

    /*private fun populateScreen() = viewModelScope.launch {
        isLoading = true


    }
*/

    private fun fetchReadingList() = viewModelScope.launch {
        readingListUsecase.fetch().collectLatest {
            _readingLists.value = it
        }
    }

    @SuppressLint("NewApi")
    fun createReadingList(image : String, name : String, description : String) = viewModelScope.launch {
        val readingListItem = ReadingListItem(
            id = generateSecureUUID() ,
            name = name ,
            description = description ,
            image = image ,
            readingList = emptyList() ,
            date = getCurrentDateAndTime() ,
            starred = false
        )
        readingListsRepository.createReadingList(readingListItem)
        fetchReadingList()
    }

    fun deleteReadingList(readingListId : String) = viewModelScope.launch{
        readingListsRepository.deleteReadingList(readingListId)
    }

   fun getSuggestedBooks() = viewModelScope.launch{
        isFetchingSuggestions = true
        suggestionsErrorMessage = ""
        when(val booksResource = fetchSuggestedBooksUsecase.invoke()) {
            is Resource.Success -> {
                suggestionsErrorMessage = ""
                isFetchingSuggestions = false
                _suggestedBooksList.value = booksResource.data ?: emptyList()
            }
            is Resource.Error -> {
                isFetchingSuggestions = false
                Timber.tag(TAG).d(booksResource.message ?: "Could not fetch suggested books from the server.")
                suggestionsErrorMessage = booksResource.message ?: "Could not fetch suggested books from the server. Please try again."
            }
            else ->{}
        }
    }

    private fun getBooksWithProgress() = viewModelScope.launch {
        getBooksAndProgressUsecase.invoke().collectLatest { booksList ->
            isLoading = false
            _booksWithProgress.value = booksList
        }
    }

    sealed class MyLibraryScreenUiEvents {
        object DismissReadListDialog : MyLibraryScreenUiEvents()
        object DefaultState : MyLibraryScreenUiEvents()
    }
}