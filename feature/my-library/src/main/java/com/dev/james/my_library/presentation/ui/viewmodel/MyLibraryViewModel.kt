package com.dev.james.my_library.presentation.ui.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.james.booktracker.core.common_models.LibraryBookData
import com.dev.james.booktracker.core.common_models.ReadingListItem
import com.dev.james.booktracker.core.common_models.ReadingLists
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.common_models.BookSave
import com.dev.james.booktracker.core.common_models.mappers.mapToLibraryData
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.core.utilities.convertToOrganisedString
import com.dev.james.booktracker.core.utilities.generateSecureUUID
import com.dev.james.booktracker.core.utilities.getCurrentDateAndTime
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.reading_lists.ReadingListsRepository
import com.dev.james.domain.usecases.FetchActiveBookProgress
import com.dev.james.domain.usecases.FetchSuggestedBooksUsecase
import com.dev.james.domain.usecases.GetBooksAndProgressUsecase
import com.dev.james.domain.usecases.ReadingListUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject



@HiltViewModel
class MyLibraryViewModel @Inject constructor(
    private val getBooksAndProgressUsecase: GetBooksAndProgressUsecase,
    private val fetchSuggestedBooksUsecase: FetchSuggestedBooksUsecase,
    private val readingListUsecase: ReadingListUsecase,
    private val readingListsRepository: ReadingListsRepository,
    private val fetchActiveBookProgress: FetchActiveBookProgress,
    private val booksRepository: BooksRepository
    ) : ViewModel() {

    companion object {
        const val TAG = "MyLibraryViewModel"
    }

    private var _booksWithProgress : MutableStateFlow<List<LibraryBookData>> = MutableStateFlow(value = listOf(LibraryBookData()))
    val booksWithProgress get() = _booksWithProgress.asStateFlow()

    private var m_baseBooksListData : MutableStateFlow<List<BaseBookData>> = MutableStateFlow(value = listOf(
        BaseBookData()
    ))
    val suggestedBooksList get() = m_baseBooksListData.asStateFlow()

    var isLoading by mutableStateOf(value = false)
        private set
    var suggestionsErrorMessage by mutableStateOf(value = "")
        private set

    var isFetchingSuggestions by mutableStateOf(value = false)
        private set

    private var _readingLists : MutableStateFlow<List<ReadingLists>> = MutableStateFlow(listOf())
    val readingLists get() = _readingLists.asStateFlow()

    private val _uiEvents : Channel<MyLibraryScreenUiEvents> = Channel<MyLibraryScreenUiEvents>()
    val uiEvents get() = _uiEvents.receiveAsFlow()


    val myWishList = booksRepository.getWishList()
        .stateIn(
            scope = viewModelScope ,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L) ,
            initialValue = listOf()
        )

    init {
        isLoading = true
        getBooksWithProgress()
        getSuggestedBooks()
        //fetchReadingList()
    }

    /*private fun populateScreen() = viewModelScope.launch {
        isLoading = true


    }
*/

   @SuppressLint("NewApi")
   fun fetchReadingList() = viewModelScope.launch {
       Timber.tag(TAG).d("fetchReadingList triggered")
        /*  val readingListFlow =  withContext(Dispatchers.Main) {
               readingListUsecase.fetch()
           }
       readingListFlow.collectLatest { readingLists ->
            _readingLists.value = readingLists
       }*/
       val readingLists = readingListsRepository.getReadingLists()
       Timber.tag(TAG).d(readingLists.first().toString())
       readingLists.map { list ->
           list.map { item ->
               val differedBooks = item.readingList.map {
                   async { fetchActiveBookProgress.invoke(it) }
               }
               val booksListWithData = differedBooks.awaitAll().map {
                   it.mapToLibraryData()
               }

               ReadingLists(
                   id = item.id ,
                   name = item.name ,
                   image = item.image ,
                   description = item.description ,
                   books = booksListWithData ,
                   date = item.date ,
                   starred = item.starred
               )
           }
       }.collectLatest { newList ->
           _readingLists.value = newList
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
                val suggestedBooks = booksResource.data
                m_baseBooksListData.value = suggestedBooks ?: emptyList()
            }
            is Resource.Error -> {
                isFetchingSuggestions = false
                Timber.tag(TAG).d(booksResource.message ?: "Could not fetch suggested books from the server.")
                suggestionsErrorMessage = booksResource.message ?: "Could not fetch suggested books from the server. Please try again."
            }
            else ->{}
        }
    }

    fun addOrRemoveFromWishList(baseBookData: BaseBookData) = viewModelScope.launch {
        val result = booksRepository.addOrRemoveFromWishList(baseBookData)
        Timber.tag(TAG).d(result)
    }


    fun saveBookToDatabase(
        baseBookData: BaseBookData ,
        chapters : Int
    ) = viewModelScope.launch {
        val bookSave = BookSave(
            bookId = baseBookData.id ,
            bookImage = baseBookData.image ,
            bookTitle = baseBookData.title ,
            bookAuthors = baseBookData.authors ,
            bookDescription = baseBookData.description ,
            bookSmallThumbnail = "",
            bookPagesCount = baseBookData.pages ,
            publisher = baseBookData.publisher ,
            publishedDate = baseBookData.publishedDate ,
            isUri = false ,
            category = baseBookData.category.convertToOrganisedString() ,
            chapters = chapters
        )
        val result = booksRepository.saveBookToDatabase(bookSave)
        if(result){
            Timber.tag(TAG).d("Book saved to database.")
            addOrRemoveFromWishList(baseBookData)
        }else {
            Timber.tag(TAG).d("Could not save to db.")

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