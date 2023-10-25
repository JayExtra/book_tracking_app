package com.dev.james.booktracker.home.presentation.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.components.CameraView
import com.dev.james.booktracker.home.presentation.components.GoogleBooksSearchBottomSheet
import com.dev.james.booktracker.home.presentation.forms.OverallGoalsForm
import com.dev.james.booktracker.home.presentation.viewmodels.ImageSelectorUiState
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenState
import com.dsc.form_builder.BaseState
import com.dsc.form_builder.ChoiceState
import com.dsc.form_builder.FormState
import com.dsc.form_builder.SelectState
import com.dsc.form_builder.TextFieldState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


private lateinit var outputDirectory: File
private lateinit var cameraExecutor: ExecutorService

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Destination
fun ReadGoalScreen(
    homeNavigator: HomeNavigator,
    readGoalsScreenViewModel: ReadGoalsScreenViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val eventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    cameraExecutor.shutdown()
                }

                Lifecycle.Event.ON_CREATE -> {
                    outputDirectory = context.getDirectory()
                    cameraExecutor = Executors.newSingleThreadExecutor()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(eventObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(eventObserver)
        }
    }


    val currentReadFormState by remember { mutableStateOf(readGoalsScreenViewModel.currentReadFormState) }

    val overallGoalsFormState by remember { mutableStateOf(readGoalsScreenViewModel.overallGoalFormState) }

    val specificGoalsFormState by remember { mutableStateOf(readGoalsScreenViewModel.specificGoalsFormState) }

    var shouldShowCameraScreen by remember { mutableStateOf(false) }


    val imageSelectorState = readGoalsScreenViewModel.imageSelectorUiState
        .collectAsStateWithLifecycle(
            initialValue = ImageSelectorUiState(),
            minActiveState = Lifecycle.State.STARTED
        )

    val readGoalsScreenUiState = readGoalsScreenViewModel.readGoalsScreenUiState
        .collectAsStateWithLifecycle(
            initialValue = ReadGoalsScreenState(),
            minActiveState = Lifecycle.State.STARTED
        )
    val selectedBookState = readGoalsScreenViewModel.selectedBookState
        .collectAsStateWithLifecycle()


    var checkedState by remember { mutableStateOf(false) }

    var isCameraButtonClicked by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val cameraPermissionRationalDialogState = rememberMaterialDialogState()

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
    val coroutineScope = rememberCoroutineScope()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission has been granted", Toast.LENGTH_SHORT).show()
                //launch camera picker screen here
                shouldShowCameraScreen = true
            } else {
                cameraPermissionRationalDialogState.show()
            }
        }


    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(key1 = !isCameraButtonClicked) {
        //check if user has granted permission
        if (isCameraButtonClicked) {
            launcher.launch(Manifest.permission.CAMERA)
        } else {
            isCameraButtonClicked = false
        }
    }

    LaunchedEffect(key1 = true  ){
        readGoalsScreenViewModel.readGoalsScreenUiEvents
            .collect { event ->
                when (event) {
                    is ReadGoalsScreenViewModel.ReadGoalsUiEvents.ShowSnackBar -> {
                        if (event.isSaving) {
                            Timber.tag("ReadGoalsScreen").d("Save successful showing Snackbar")
                            coroutineScope.launch {
                                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message,
                                    actionLabel = "Undo" ,
                                    duration = SnackbarDuration.Short
                                )
                                when (snackBarResult) {
                                    SnackbarResult.Dismissed -> {
                                        //do nothing
                                        Timber.tag("ReadGoalsScreen").d("SnackBar dismissed")
                                    }
                                    SnackbarResult.ActionPerformed -> {
                                        readGoalsScreenViewModel.passMainScreenActions(
                                            ReadGoalsScreenViewModel.ReadGoalsUiActions
                                                .UndoBookSave
                                        )
                                    }
                                }
                            }

                        } else {

                            Timber.tag("ReadGoalsScreen").d("Just a normal snackbar message")

                            scaffoldState.snackbarHostState.showSnackbar(
                                message = event.message,
                                withDismissAction = true  ,
                                duration = SnackbarDuration.Short
                            )

                        }
                    }
                    is ReadGoalsScreenViewModel.ReadGoalsUiEvents.navigateToHome -> {
                        homeNavigator.openHomeScreen()
                    }

                    else -> {}
                }
            }

    }



    if (shouldShowCameraScreen) {
        CameraView(
            outputDirectory = outputDirectory,
            executor = cameraExecutor,
            onImageCaptured = { uri ->
                //update the image state in view model
                readGoalsScreenViewModel.passAddReadFormAction(
                    ReadGoalsScreenViewModel
                        .AddReadFormUiActions.ImageSelected(
                            imageUri = uri
                        )
                )
                //hide camera
                shouldShowCameraScreen = false

            },
            onError = { error ->
                Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
            },
            onCloseAction = {
                shouldShowCameraScreen = false
                readGoalsScreenViewModel.passAddReadFormAction(
                    ReadGoalsScreenViewModel.AddReadFormUiActions
                        .DismissImagePicker
                )
            }
        )

    } else {

        BottomSheetScaffold(
            modifier = Modifier.testTag("read_goals_screen_scaffold"),
            sheetPeekHeight = 0.dp,
            scaffoldState = scaffoldState,
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
                        Text("Search for book")

                        IconButton(
                            onClick = {
                                keyboardController?.hide()
                                readGoalsScreenViewModel.cancelQueryJob()
                                coroutineScope.launch {
                                    sheetState.hide()
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
            sheetShape = RoundedCornerShape(0.dp),
            sheetTonalElevation = 3.dp,
            sheetSwipeEnabled = false,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp),
                    contentAlignment = Alignment.Center
                ) {
                    //call our google bottom sheet here
                    GoogleBooksSearchBottomSheet()
                }
            }
        ) {


            StatelessReadGoalScreen(
                currentReadFormState = currentReadFormState,
                uiState = readGoalsScreenUiState.value,
                overallGoalsFormState = overallGoalsFormState,
                specificGoalsFormState = specificGoalsFormState,
                alertSwitchState = checkedState,
                popBackStack = {
                    if (sheetState.isVisible) {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    } else {
                        homeNavigator.openHomeScreen()
                    }
                },
                /*
                onGoogleIconClicked = {
                    //open botton sheet with books info
                    //readGoalsScreenViewModel.searchForBook()
                    coroutineScope.launch {
                        if (sheetState.isVisible) {
                            sheetState.hide()
                        } else {
                            sheetState.expand()
                        }
                    }
                },
                imageSelectorState = imageSelectorState.value,
               onSaveClicked = {

                    val currentReadFormTitleFieldState: TextFieldState =
                        currentReadFormState.getState("title")
                    val currentReadFormAuthorFieldState: TextFieldState =
                        currentReadFormState.getState("author")
                    val currentReadFormPagesFieldState: TextFieldState =
                        currentReadFormState.getState("pages_count")
                    val currentReadFormChaptersState: TextFieldState =
                        currentReadFormState.getState("chapters")
                    val currentReadFormCurrentChapter: TextFieldState =
                        currentReadFormState.getState("current chapter")
                    val currentReadFormCurChaptTitleState: TextFieldState =
                        currentReadFormState.getState("chapter title")

                    val formValidationResult = currentReadFormState.validate()

                    readGoalsScreenViewModel.validateImageSelected(
                        imageUri = imageSelectorState.value.imageSelectedUri ,
                        imageUrl = imageSelectorState.value.imageUrl
                    )

                    if (formValidationResult && !imageSelectorState.value.isError) {

                        if (currentReadFormCurrentChapter.value.toInt() > currentReadFormChaptersState.value.toInt()) {
                            Toast.makeText(
                                context,
                                "The current chapter you are in exceeds the total chapters in this book",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            //start saving process to db
                            Toast.makeText(
                                context,
                                "saving data...",
                                Toast.LENGTH_SHORT
                            )
                                .show()



                            val author = currentReadFormAuthorFieldState.value
                            val title = currentReadFormTitleFieldState.value
                            val pages = currentReadFormPagesFieldState.value
                            val chapters = currentReadFormChaptersState.value
                            val currentChapter = currentReadFormCurrentChapter.value
                            val chapterTitle = currentReadFormCurChaptTitleState.value

                            val bookId = if (imageSelectorState.value.imageSelectedUri != Uri.EMPTY)
                                generateSecureUUID()
                            else
                                selectedBookState.value.bookId ?: "n/a"


                            val isUri = imageSelectorState.value.imageSelectedUri != Uri.EMPTY

                            val bookImage =
                                if (isUri) imageSelectorState.value.imageSelectedUri.toString() else imageSelectorState.value.imageUrl

                            val smallThumbnail = selectedBookState.value.bookSmallThumbnail
                            val publisher = selectedBookState.value.publisher
                            val publishedDate = selectedBookState.value.publishedDate

                            val bookSave = BookSave(
                                bookId = bookId,
                                bookImage = bookImage,
                                bookTitle = title,
                                bookAuthors = author,
                                bookSmallThumbnail = smallThumbnail ?: "n/a",
                                bookPagesCount = pages.toInt(),
                                publisher = publisher ?: "n/a",
                                publishedDate = publishedDate ?: "n/a",
                                isUri = isUri,
                                chapters = chapters.toInt(),
                                currentChapter = currentChapter.toInt(),
                                currentChapterTitle = chapterTitle
                            )

                            readGoalsScreenViewModel.passAddReadFormAction(
                                ReadGoalsScreenViewModel.AddReadFormUiActions.SaveBook(
                                    bookSave = bookSave
                                )
                            )
                        }
                    }

                },*/
                onImageSelectorClicked = {


                    //update selector state to show circular progress
                    readGoalsScreenViewModel.passAddReadFormAction(
                        ReadGoalsScreenViewModel.AddReadFormUiActions.LaunchImagePicker
                    )

                    when {
                        cameraPermissionState.status.isGranted -> {
                            //if so launch image picker
                            Toast.makeText(
                                context,
                                "Starting camera",
                                Toast.LENGTH_SHORT
                            ).show()
                            shouldShowCameraScreen = true

                        }

                        cameraPermissionState.status == PermissionStatus.Denied(shouldShowRationale = false) -> {
                            isCameraButtonClicked = true
                        }

                        cameraPermissionState.status.shouldShowRationale -> {
                            cameraPermissionRationalDialogState.show()
                        }

                    }


                },
                onClearImage = {
                    readGoalsScreenViewModel.passAddReadFormAction(
                        ReadGoalsScreenViewModel.AddReadFormUiActions
                            .ClearImage
                    )
                },
                onAlertSwitchChecked = { status ->
                    checkedState = status
                },

               /* onNextClicked = {

                    val supportedDays = listOf(
                        "Select specific days", "Every day except"
                    )

                    when (readGoalsScreenUiState.value.currentPosition) {
                        0 -> {

                            val allChaptersState =
                                currentReadFormState.getState<TextFieldState>("chapters")
                            val currentChaptersState =
                                currentReadFormState.getState<TextFieldState>("current chapter")

                            readGoalsScreenViewModel.validateImageSelected(
                                imageUri = imageSelectorState.value.imageSelectedUri ,
                                imageUrl = imageSelectorState.value.imageUrl
                            )

                            if (currentReadFormState.validate() && !imageSelectorState.value.isError) {
                                if (currentChaptersState.value.toInt() > allChaptersState.value.toInt()) {
                                    Toast.makeText(
                                        context,
                                        "The current chapter you are in exceeds the total chapters in this book",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    readGoalsScreenViewModel.passMainScreenActions(
                                        action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                            currentPosition = readGoalsScreenUiState.value.currentPosition
                                        )
                                    )
                                }

                            }
                        }

                        1 -> {
                            val timeFieldState =
                                overallGoalsFormState.getState<TextFieldState>("time")
                            val freqFieldState =
                                overallGoalsFormState.getState<ChoiceState>("frequency field")
                            val daysSelectedState =
                                overallGoalsFormState.getState<SelectState>("specific days")

                            if (timeFieldState.validate() && freqFieldState.validate()) {
                                if (supportedDays.contains(freqFieldState.value)) {
                                    if (daysSelectedState.validate()) {
                                        readGoalsScreenViewModel.passMainScreenActions(
                                            action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                                currentPosition = readGoalsScreenUiState.value.currentPosition
                                            )
                                        )
                                    }
                                } else {
                                    readGoalsScreenViewModel.passMainScreenActions(
                                        action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                            currentPosition = readGoalsScreenUiState.value.currentPosition
                                        )
                                    )
                                }
                            }
                        }

                        2 -> {

                            val bookMonthsFieldState =
                                specificGoalsFormState.getState<TextFieldState>("books_month")
                            val timeChapterFieldState =
                                specificGoalsFormState.getState<TextFieldState>("time_chapter")
                            val periodChoiceState =
                                specificGoalsFormState.getState<ChoiceState>("period")
                            val availableBooksState =
                                specificGoalsFormState.getState<ChoiceState>("available_books")
                            val periodDaysSelectState =
                                specificGoalsFormState.getState<SelectState>("period_days")


                            if (bookMonthsFieldState.validate() && availableBooksState.validate()) {
                                if (timeChapterFieldState.validate() && periodChoiceState.validate()) {
                                    if (supportedDays.contains(periodChoiceState.value)) {
                                        if (periodDaysSelectState.validate()) {
                                            Toast.makeText(
                                                context,
                                                "saving with specific stated days",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            readGoalsScreenViewModel.passMainScreenActions(
                                                action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                                    currentPosition = readGoalsScreenUiState.value.currentPosition
                                                )
                                            )
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "saving without specific days",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        readGoalsScreenViewModel.passMainScreenActions(
                                            action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MoveNext(
                                                currentPosition = readGoalsScreenUiState.value.currentPosition
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
                onPreviousClicked = {
                    readGoalsScreenViewModel.passMainScreenActions(
                        action = ReadGoalsScreenViewModel.ReadGoalsUiActions.MovePrevious(
                            currentPosition = readGoalsScreenUiState.value.currentPosition
                        )
                    )
                } ,

                callSavedBooks = {
                    readGoalsScreenViewModel.getCachedBooks()
                } ,*/
                saveGoalToDatabase = {
                    val supportedDays = listOf(
                        "Select specific days", "Every day except"
                    )

                    val timeFieldState =
                        overallGoalsFormState.getState<TextFieldState>("time")
                    val freqFieldState =
                        overallGoalsFormState.getState<ChoiceState>("frequency field")
                    val daysSelectedState =
                        overallGoalsFormState.getState<SelectState>("specific days")
                    val booksCountState: TextFieldState =
                        overallGoalsFormState.getState("books_month")

                    if (timeFieldState.validate() && freqFieldState.validate() && booksCountState.validate()) {
                        if (supportedDays.contains(freqFieldState.value)) {
                            if (daysSelectedState.validate()) {
                                readGoalsScreenViewModel.passMainScreenActions(
                                    action = ReadGoalsScreenViewModel.ReadGoalsUiActions.SaveGoalToDatabase
                                )
                            }
                        } else {
                            readGoalsScreenViewModel.passMainScreenActions(
                                action = ReadGoalsScreenViewModel.ReadGoalsUiActions.SaveGoalToDatabase
                            )
                        }
                    }

                }
            )

        }

    }


    MaterialDialog(
        dialogState = cameraPermissionRationalDialogState,
        backgroundColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(10.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {

            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = "warning image",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )

            val rationalText =
                if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
                    //camera permission is fully denied by user
                    "Camera permission is required for you to be able to take book cover images. Redirecting you to the settings screen to grant this permission."
                } else {
                    "Camera permission is required for you to be able to take book cover images.Please grant this permission then try again."
                }

            Text(
                text = rationalText,
                style = BookAppTypography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            RoundedBrownButton(
                label = "Grant permission.",
                onClick = {
                    if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
                        //camera permission is fully denied by user
                        val intent = Intent(Settings.ACTION_APPLICATION_SETTINGS).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        context.startActivity(
                            intent
                        )

                    } else {
                        launcher.launch(Manifest.permission.CAMERA)
                    }
                    cameraPermissionRationalDialogState.hide()
                }
            )
            Spacer(modifier = Modifier.height(20.dp))

        }

    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "ReadGoalScreen", showBackground = true)
fun StatelessReadGoalScreen(
    context: Context = LocalContext.current,
    currentReadFormState: FormState<TextFieldState> = FormState(fields = listOf()),
    overallGoalsFormState: FormState<BaseState<out Any>> = FormState(fields = listOf()),
    specificGoalsFormState: FormState<BaseState<out Any>> = FormState(fields = listOf()),
    uiState: ReadGoalsScreenState = ReadGoalsScreenState(),
    imageSelectorState: ImageSelectorUiState = ImageSelectorUiState(),
    alertSwitchState: Boolean = false,
    popBackStack: () -> Unit = {},
    onGoogleIconClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onImageSelectorClicked: () -> Unit = {},
    onClearImage: () -> Unit = {},
    onNextClicked: () -> Unit = {},
    onPreviousClicked: () -> Unit = {},
    onAlertSwitchChecked: (Boolean) -> Unit = {} ,
    callSavedBooks : () -> Unit = {} ,
    saveGoalToDatabase :() -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        StandardToolBar(
            /*navActions = {
                //control visibility depending on where we are
                if (uiState.currentPosition == 0) {
                    Button(
                        onClick = {
                            onGoogleIconClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google_icon_24),
                            contentDescription = "google icon for searching books",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        )
                    }
                }

            },*/
            navigate = {
                //navigate back to home screen
                popBackStack()
            }
        ) {
            
            Text(
                text = "Set goals",
                style = BookAppTypography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.weight(1f)
        ) {

           /* if(uiState.currentPosition == 2){
               callSavedBooks()
            }*/

          /*  androidx.compose.animation.AnimatedVisibility(
                visible = uiState.currentPosition == 0,
                enter = fadeIn() + slideInHorizontally { if (uiState.currentPosition > uiState.previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (uiState.currentPosition > uiState.previousPosition) -it else it }
            ) {
                CurrentReadForm(
                    modifier = Modifier.fillMaxHeight(),
                    currentReadFormState = currentReadFormState,
                    onSaveBookClicked = {
                        onSaveClicked()
                    },
                    imageSelectorState = imageSelectorState,
                    imageSelectorClicked = {
                        onImageSelectorClicked()
                    },
                    onClearImage = {
                        onClearImage()
                    }
                )
            }*/

            androidx.compose.animation.AnimatedVisibility(
                visible = uiState.currentPosition == 0,
                enter = fadeIn() + slideInHorizontally { if (uiState.currentPosition > uiState.previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (uiState.currentPosition > uiState.previousPosition) -it else it }

            ) {
                OverallGoalsForm(
                    overallGoalsFormState = overallGoalsFormState,
                    alertSwitchState = alertSwitchState,
                    onAlertSwitchChecked = { status ->
                        onAlertSwitchChecked(status)
                    } ,
                    onSaveGoal = {
                        //save goal to db
                        saveGoalToDatabase()
                    }
                )
            }

           /* androidx.compose.animation.AnimatedVisibility(
                visible = uiState.currentPosition == 2,
                enter = fadeIn() + slideInHorizontally { if (uiState.currentPosition > uiState.previousPosition) it else -it },
                exit = fadeOut() + slideOutHorizontally { if (uiState.currentPosition > uiState.previousPosition) -it else it }

            ) {

                SpecificGoalsForm(
                    specificGoalsFormState = specificGoalsFormState ,
                    readGoalsScreenUiState = uiState
                )
            }*/
        }

       /* BottomNextPreviousButtons(
            modifier = Modifier.padding(16.dp),
            currentPosition = uiState.currentPosition,
            onNextClicked = {
                onNextClicked()
            },
            onPreviousClicked = {
                onPreviousClicked()
            } ,
            disableNext = uiState.shouldDisableNextButton
        )*/

    }

}

private fun Context.getDirectory(): File {
    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
        File(
            it,
            this.resources.getString(R.string.app_name)
        ).apply {
            mkdirs()
        }
    }
    return if (mediaDir != null && mediaDir.exists()) mediaDir else this.filesDir
}


//camera permissions




