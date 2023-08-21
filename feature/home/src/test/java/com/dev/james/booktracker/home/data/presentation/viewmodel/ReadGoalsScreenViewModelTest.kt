package com.dev.james.booktracker.home.data.presentation.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import com.dev.james.booktracker.core.test_commons.MainCoroutineRule
import com.dev.james.booktracker.core.test_commons.getFakeBookSaveWithUri
import com.dev.james.booktracker.home.domain.repositories.BooksRepository
import com.dev.james.booktracker.home.domain.repositories.GoalsRepository
import com.dev.james.booktracker.home.presentation.viewmodels.ImageSelectorUiState
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import com.dsc.form_builder.TextFieldState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ReadGoalsScreenViewModelTest {

    private lateinit var goalsRepository: GoalsRepository
    private lateinit var booksRepository: BooksRepository
    private lateinit var readGoalsScreenViewModel: ReadGoalsScreenViewModel
    private val testUrl = "https://someprovidedurl.com/image/djoad4u250-594tc-dvlsdvnsdvc0r9.jpg"
    private val testUri = "app:home/decks/local/something".toUri()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
        goalsRepository = Mockito.mock(GoalsRepository::class.java)
        booksRepository = Mockito.mock(BooksRepository::class.java)

        readGoalsScreenViewModel = ReadGoalsScreenViewModel(
            booksRepository = booksRepository ,
            goalsRepository = goalsRepository
        )
    }

    @Test
    fun validateImageSelected_withImageUri_returnsNoError() = runTest {
        readGoalsScreenViewModel.validateImageSelected(
            imageUri = testUri ,
            imageUrl = ""
        )
        val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorState.isError).isFalse()
    }

    @Test
    fun validateImageSelected_withImageUrl_returnsNoError() = runTest {
        readGoalsScreenViewModel.validateImageSelected(
            imageUri = Uri.EMPTY ,
            imageUrl = testUrl
        )
        val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorState.isError).isFalse()
    }

    @Test
    fun validateImageSelected_withEitherUriOrUrlEmpty_returnsError() = runTest {
        readGoalsScreenViewModel.validateImageSelected(
            imageUri = Uri.EMPTY ,
            imageUrl = ""
        )
        val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorState.isError).isTrue()
    }


    @Test
    fun passAddReadFormAction_withLaunchImagePickerAction_showsProgressBar() = runTest {
        readGoalsScreenViewModel.passAddReadFormAction(ReadGoalsScreenViewModel
            .AddReadFormUiActions.LaunchImagePicker)

        val imageSelectorUiState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorUiState.showProgress).isTrue()
    }

    @Test
    fun passAddReadFormAction_withDismissImagePickerAction_hidesProgressBar() = runTest {
        readGoalsScreenViewModel.passAddReadFormAction(ReadGoalsScreenViewModel
            .AddReadFormUiActions.DismissImagePicker)

        val imageSelectorUiState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorUiState.showProgress).isFalse()

    }

    @Test
    fun passAddReadFormAction_withImageSelected_updatesImageSelectorUiState_withSelectedImage() = runTest {
        readGoalsScreenViewModel.passAddReadFormAction(
            ReadGoalsScreenViewModel
                .AddReadFormUiActions.ImageSelected(imageUri = testUri)
        )

        val imageSelectorUiState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorUiState.imageSelectedUri).isNotEqualTo(Uri.EMPTY)
    }

    @Test
    fun passAddReadFormAction_withClearImageAction_updatesImageSelectorUiState_withEmptyUri() = runTest {
        readGoalsScreenViewModel.passAddReadFormAction(
            ReadGoalsScreenViewModel.AddReadFormUiActions.ClearImage
        )

        val imageSelectorUiState = readGoalsScreenViewModel.imageSelectorUiState.value
        assertThat(imageSelectorUiState.imageSelectedUri).isEqualTo(Uri.EMPTY)

    }

    @Test
    fun passAddReadFormAction_withSaveBookAction_savesBookToDatabase_updatesScreenUiEvents() = runTest {

        readGoalsScreenViewModel.passAddReadFormAction(ReadGoalsScreenViewModel.AddReadFormUiActions.SaveBook(
            bookSave = getFakeBookSaveWithUri()
        ))

        Mockito.`when`(booksRepository.saveBookToDatabase(getFakeBookSaveWithUri())).thenReturn(true)

        val event = readGoalsScreenViewModel.readGoalsScreenUiEvents.first()

        when(event){
            is ReadGoalsScreenViewModel.ReadGoalsUiEvents.ShowSnackBar -> {
                assertThat(event.isSaving).isTrue()
            }
            else -> {}
        }

    }


}