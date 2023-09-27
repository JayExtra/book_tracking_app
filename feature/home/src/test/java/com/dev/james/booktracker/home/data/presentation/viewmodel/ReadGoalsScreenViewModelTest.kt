package com.dev.james.booktracker.home.data.presentation.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import com.dev.james.booktracker.core.test_commons.MainCoroutineRule
import com.dev.james.booktracker.core.test_commons.getFakeBookSaveWithUrl
import com.dev.james.booktracker.core.test_commons.getTestBookVolumeDto
import com.dev.james.booktracker.core.utilities.Resource
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import com.dev.james.domain.repository.home.BooksRepository
import com.dev.james.domain.repository.home.GoalsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.given
import org.robolectric.RobolectricTestRunner
import retrofit2.HttpException
import retrofit2.Response
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

@RunWith(RobolectricTestRunner::class)
class ReadGoalsScreenViewModelTest {

    private lateinit var goalsRepository: GoalsRepository
    private lateinit var booksRepository: BooksRepository

    private lateinit var readGoalsScreenViewModel: ReadGoalsScreenViewModel
    private val testUrl = "https://someprovidedurl.com/image/djoad4u250-594tc-dvlsdvnsdvc0r9.jpg"
    private val testUri = "app:home/decks/local/something".toUri()
    private lateinit var testScope: TestScope

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        val dispatcher = StandardTestDispatcher(
            name = "book_tracker_test_dispatcher"
        )

        testScope = TestScope(dispatcher)
        goalsRepository = Mockito.mock(GoalsRepository::class.java)
        booksRepository = Mockito.mock(BooksRepository::class.java)

        readGoalsScreenViewModel = ReadGoalsScreenViewModel(
            booksRepository = booksRepository,
            goalsRepository = goalsRepository
        )
    }

    @Test
    fun validateImageSelected_withImageUri_returnsNoError() = runTest {
        readGoalsScreenViewModel.validateImageSelected(
            imageUri = testUri,
            imageUrl = ""
        )
        val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorState.isError).isFalse()
    }

    @Test
    fun validateImageSelected_withImageUrl_returnsNoError() = runTest {
        readGoalsScreenViewModel.validateImageSelected(
            imageUri = Uri.EMPTY,
            imageUrl = testUrl
        )
        val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorState.isError).isFalse()
    }

    @Test
    fun validateImageSelected_withEitherUriOrUrlEmpty_returnsError() = runTest {
        readGoalsScreenViewModel.validateImageSelected(
            imageUri = Uri.EMPTY,
            imageUrl = ""
        )
        val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorState.isError).isTrue()
    }


    @Test
    fun passAddReadFormAction_withLaunchImagePickerAction_showsProgressBar() = runTest {
        readGoalsScreenViewModel.passAddReadFormAction(
            ReadGoalsScreenViewModel
                .AddReadFormUiActions.LaunchImagePicker
        )

        val imageSelectorUiState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorUiState.showProgress).isTrue()
    }

    @Test
    fun passAddReadFormAction_withDismissImagePickerAction_hidesProgressBar() = runTest {
        readGoalsScreenViewModel.passAddReadFormAction(
            ReadGoalsScreenViewModel
                .AddReadFormUiActions.DismissImagePicker
        )

        val imageSelectorUiState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorUiState.showProgress).isFalse()

    }

    @Test
    fun passAddReadFormAction_withImageSelected_updatesImageSelectorUiState_withSelectedImage() =
        runTest {
            readGoalsScreenViewModel.passAddReadFormAction(
                ReadGoalsScreenViewModel
                    .AddReadFormUiActions.ImageSelected(imageUri = testUri)
            )

            val imageSelectorUiState = readGoalsScreenViewModel.imageSelectorUiState.value
            assertThat(imageSelectorUiState.imageSelectedUri).isNotEqualTo(Uri.EMPTY)
        }

    @Test
    fun passAddReadFormAction_withClearImageAction_updatesImageSelectorUiState_withEmptyUri() =
        runTest {
            readGoalsScreenViewModel.passAddReadFormAction(
                ReadGoalsScreenViewModel.AddReadFormUiActions.ClearImage
            )

            val imageSelectorUiState = readGoalsScreenViewModel.imageSelectorUiState.value
            assertThat(imageSelectorUiState.imageSelectedUri).isEqualTo(Uri.EMPTY)

        }

    @OptIn(ExperimentalTime::class)
    @Test
    fun passBookFormAction_withSaveAction_savesBookToDatabase_shouldUpdateShowSnackBarUiEvent() {
        runTest(timeout = 12000L.milliseconds) {
            val book = getFakeBookSaveWithUrl()
            Mockito.`when`(booksRepository.saveBookToDatabase(book))
                .thenReturn(true)

            readGoalsScreenViewModel.passAddReadFormAction(
                ReadGoalsScreenViewModel.AddReadFormUiActions.SaveBook(
                    bookSave = book
                )
            )


            val event = readGoalsScreenViewModel.readGoalsScreenUiEvents.first()

            when (event) {
                is ReadGoalsScreenViewModel.ReadGoalsUiEvents.ShowSnackBar -> {
                    assertThat(event.isSaving).isTrue()
                }

                else -> {}
            }

        }
    }

    @Test
    fun searchForBook_withQueryString_returnsListOfBooksResponse() = testScope.runTest {
        val testBookResponse = getTestBookVolumeDto()
        Mockito.`when`(booksRepository.getBooksFromApi("Some title", ""))
            .thenReturn(
                flow {
                    emit(Resource.Success(data = testBookResponse))
                }
            )

        readGoalsScreenViewModel.searchForBook("Some title")
        when (val state = readGoalsScreenViewModel.googleBottomSheetSearchState.first()) {
            is ReadGoalsScreenViewModel.GoogleBottomSheetUiState.HasFetched -> {
                assertThat(state.booksList).isNotEmpty()
            }

            else -> {}
        }

    }

    @Test
    fun searchForBook_encounteringError_returnsErrorMessage() = testScope.runTest {
        given(booksRepository.getBooksFromApi("Some title" , "")).willAnswer {
            val response: Response<String> = Response.error(500, "Error reaching server.".toResponseBody("plain/text".toMediaTypeOrNull()))
            throw HttpException(response)
        }

        readGoalsScreenViewModel.searchForBook("Some title")

        when (val state = readGoalsScreenViewModel.googleBottomSheetSearchState.first()) {
            is ReadGoalsScreenViewModel.GoogleBottomSheetUiState.HasFailed -> {
                assertThat(state.errorMessage).isNotEmpty()
            }
            else -> {}
        }

    }

    @Test
    fun moveNextTriggered_updatesCurrentPosition() = testScope.runTest {
        readGoalsScreenViewModel.passMainScreenActions(
            action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(currentPosition = 0)
        )

        val event = readGoalsScreenViewModel.readGoalsScreenUiState.first()

        assertThat(event.currentPosition).isGreaterThan(0)

    }

    @Test
    fun movePreviousTriggered_updatesCurrentPosition() = testScope.runTest {
        readGoalsScreenViewModel.passMainScreenActions(
            action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(currentPosition = 0)
        )
        readGoalsScreenViewModel.passMainScreenActions(
            action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(currentPosition = 1)
        )
        readGoalsScreenViewModel.passMainScreenActions(
            action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MovePrevious(currentPosition = 2)
        )

        val event = readGoalsScreenViewModel.readGoalsScreenUiState.first()

        assertThat(event.currentPosition).isLessThan(2)

    }

    @Test
    fun getCachedBooks_returnsCachedBooks() = testScope.runTest {
        Mockito.`when`(booksRepository.getSavedBooks()).thenReturn(
           flow{
                emit(
                    listOf(getFakeBookSaveWithUrl())
                )
           }
        )

        readGoalsScreenViewModel.getCachedBooks()
        val state = readGoalsScreenViewModel.readGoalsScreenUiState.value
        assertThat(state.savedBooksList).isNotEmpty()
    }


    @Test
    fun passBookFormAction_withUndoAction_deletesBookFromDb() {
        runTest(timeout = 12000L.milliseconds) {
            val book = getFakeBookSaveWithUrl()

            Mockito.`when`(booksRepository.deleteBookInDatabase(book.bookId))
                .thenReturn(true)

            readGoalsScreenViewModel.passMainScreenActions(
                action = ReadGoalsScreenViewModel.ReadGoalsUiActions.UndoBookSave
            )


            val state = readGoalsScreenViewModel.readGoalsScreenUiEvents.first()

            when (state) {
                is ReadGoalsScreenViewModel.ReadGoalsUiEvents.ShowSnackBar -> {
                    assertThat(state.isSaving).isFalse()
                }

                else -> {}
            }
        }
    }

}