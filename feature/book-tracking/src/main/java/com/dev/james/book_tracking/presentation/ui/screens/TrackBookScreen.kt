package com.dev.james.book_tracking.presentation.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.dev.james.book_tracking.R
import com.dev.james.domain.utilities.StopWatch
import com.dev.james.book_tracking.presentation.ui.navigation.BookTrackNavigation
import com.dev.james.book_tracking.presentation.viewmodel.BookTrackingViewModel
import com.dev.james.book_tracking.presentation.viewmodel.ReadingListUiItem
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.ImageSelectorUiState
import com.dev.james.booktracker.compose_ui.ui.components.BarGraph
import com.dev.james.booktracker.compose_ui.ui.components.ImageSelectorComponent
import com.dev.james.booktracker.compose_ui.ui.components.OutlinedTextFieldComponent
//import com.dev.james.booktracker.compose_ui.ui.components.SingleActionConfirmationDialog
import com.dev.james.booktracker.compose_ui.ui.components.SingleActionConfirmationPrompt
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.enums.PreviousScreenDestinations
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.utils.splitToDHMS
import com.dev.james.booktracker.core.common_models.BookProgressData
import com.dev.james.booktracker.core.common_models.BookStatsData
import com.dev.james.booktracker.core.utilities.formatTimeToDHMS
import com.ramcosta.composedestinations.annotation.Destination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@Composable
/*@Preview(
    showBackground = true,
    widthDp = 405 ,
    heightDp = 1060
)*/
@Destination
fun TrackBookScreen(
    navigation: BookTrackNavigation,
    bookTrackingViewModel: BookTrackingViewModel = hiltViewModel(),
    previousScreenDestinations: PreviousScreenDestinations,
    bookId: String?
) {
    val coroutineScope = rememberCoroutineScope()

    val finishSessionDialogState = rememberMaterialDialogState(false)

    val resetClockDialogState = rememberMaterialDialogState(false)

    val proceedToSaveDialogState = rememberMaterialDialogState(false)

    var chapterTitleFieldState by rememberSaveable {
        mutableStateOf("")
    }
    var chapterNumberFieldState by rememberSaveable {
        mutableStateOf("")
    }
    var pageNumberFieldState by rememberSaveable {
        mutableStateOf("")
    }


    /*val screenEvents = bookTrackingViewModel.trackBookScreenUiEvents.collectAsStateWithLifecycle(
        initialValue = BookTrackingViewModel.TrackBookScreenUiEvents.DefaultState
    )*/

    val bookCompletedDialogState = bookTrackingViewModel.showBookCompleteDialog

    LaunchedEffect(key1 = true) {
        bookId?.let {
            bookTrackingViewModel.getBookStatistics(it)
            bookTrackingViewModel.fetchReadingLists(it)

        }
    }

    if (bookCompletedDialogState) {
        SingleActionConfirmationPrompt(
            title = "Congratulations",
            body = "You have completed this book, great job! Do not stop here keep on reading!",
            showAnimation = true,
            onAccept = {
                //close the dialog for now
                bookTrackingViewModel.updateTotalBooksReadCount()
                bookTrackingViewModel.dismissDialog()
            },
            onCancel = {
                //close the dialog
                bookTrackingViewModel.dismissDialog()
            }
        )
    }

    /*when(screenEvents.value){
        is BookTrackingViewModel.TrackBookScreenUiEvents.DefaultState -> {}
        is BookTrackingViewModel.TrackBookScreenUiEvents.ShowBookCompleteDialog -> {

        }
    }
*/
    val stopWatch = remember {
        StopWatch()
    }

    var showTimerAndTrackCard by remember {
        mutableStateOf(false)
    }

    var isStopWatchRunning by rememberSaveable {
        mutableStateOf(false)
    }

    var isStopWatchPaused by rememberSaveable {
        mutableStateOf(false)
    }

    var isTimerValueZero by rememberSaveable {
        mutableStateOf(false)
    }
    var shouldShowReadingListDialog by rememberSaveable {
        mutableStateOf(false)
    }


    val bookData = bookTrackingViewModel.bookStatsState.collectAsStateWithLifecycle()
    val goalData = bookTrackingViewModel.goalProgressState.collectAsStateWithLifecycle()
    val readingLists = bookTrackingViewModel.readingLists.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    if (shouldShowReadingListDialog) {
        AddToReadingListDialog(
            readingList = readingLists.value,
            onDismiss = {
                shouldShowReadingListDialog = false
            },
            onListSelected = {
                //add/remove from reading list
            },
            createNewPlayList = {
                //create reading list if empty
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            StandardToolBar(
                showBackArrow = true,
                title = {
                    Text(text = "Track your progress", style = BookAppTypography.headlineSmall)
                },
                navigate = {
                    when (previousScreenDestinations) {
                        PreviousScreenDestinations.HOME_SCREEN -> {
                            navigation.backToHomeScreen()
                        }

                        PreviousScreenDestinations.LIBRARY_SCREEN -> {
                            navigation.backToMyLibraryScreen()
                        }

                        else -> {}
                    }
                },
                navActions = {
                    IconButton(
                        onClick = {
                            /*opens playlist dialog*/
                            //show dialog
                            shouldShowReadingListDialog = true
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            painter = painterResource(
                                id = com.dev.james.booktracker.compose_ui.R.drawable.ic_playlist_add
                            ),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(
                        state = rememberScrollState()
                    )
                ) {


                    BookProgressSection(
                        showTimerText = showTimerAndTrackCard,
                        bookData = bookData.value,
                        timerValue = stopWatch.formattedTime,
                        timerRunning = isStopWatchRunning,
                        isTimerPaused = isStopWatchPaused,
                        isBookActive = bookData.value.progress < 1f,
                        onShowTimerText = { show ->

                            showTimerAndTrackCard = show

                            bookId?.let {
                                bookTrackingViewModel.setActiveBook(it)
                            }

                            if (isStopWatchRunning) {
                                isStopWatchRunning = false
                                isStopWatchPaused = true
                                stopWatch.pause()
                            } else {
                                isStopWatchRunning = true
                                isStopWatchPaused = false
                                stopWatch.start()
                            }
                        },
                        onFinish = {
                            isStopWatchRunning = false
                            isStopWatchPaused = true
                            stopWatch.pause()
                            if (stopWatch.finalTime == 0L) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Reading time cannot be 0 seconds",
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else {
                                isTimerValueZero = false
                                finishSessionDialogState.show()
                            }
                        },
                        onReset = {
                            resetClockDialogState.show()
                        }
                    )

                    ExpandableDescriptionCard(
                        description = bookData.value.description
                    )

                    val pagesPerMinText = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold)
                        ) {
                            append(bookData.value.pagesPerMinute.toString())
                        }
                        //append(" ")
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        ) {
                            append("pgs/min")
                        }
                    }

                    TimeAndPageComponent(
                        pagesStatText = "${bookData.value.currentPage}/${bookData.value.totalPages}",
                        timeStatText = bookData.value.bestTime,
                        pagesPerMinStatText = pagesPerMinText
                    )

                    ProgressGraphSection(
                        bookLogs = bookData.value.logs,
                        totalTimeSpentWeekly = bookData.value.totalTimeSpentWeekly,
                        targetTime = goalData.value.goalTime
                    )
                    /*
                                AnimatedVisibility(
                                    visible = showTimerAndTrackCard ,
                                    exit = fadeOut(
                                        animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
                                    ) + slideOutVertically(
                                        animationSpec = tween(durationMillis = 500),
                                        targetOffsetY = { -it / 2 }
                                    ),
                                    enter = fadeIn(
                                        animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
                                    ) + slideInVertically(
                                        animationSpec = tween(durationMillis = 500),
                                        initialOffsetY = { -it / 2 }
                                    )
                                ){


                                }*/
                }
            }

        }
    )



    MaterialDialog(
        dialogState = finishSessionDialogState,
        shape = RoundedCornerShape(10.dp)
    ) {

        var chapterFieldErrorState by remember {
            mutableStateOf(false)
        }
        var pagesFieldErrorState by remember {
            mutableStateOf(false)
        }
        var chapterNumberFieldErrorState by remember {
            mutableStateOf(false)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            TrackSection(
                canFinishSession = isStopWatchRunning,
                errorInChapterField = chapterFieldErrorState,
                errorInPagesField = pagesFieldErrorState,
                errorInChapterNumField = chapterNumberFieldErrorState,
                onFinishSession = { chapterTitle, chapterNum, pageNum ->

                    if (chapterNum.isEmpty()) {
                        chapterNumberFieldErrorState = true
                    } else if (pageNum.isEmpty()) {
                        pagesFieldErrorState = true
                    } else if (chapterTitle.isEmpty()) {
                        chapterFieldErrorState = true

                    } else {
                        chapterFieldErrorState = false
                        chapterNumberFieldErrorState = false
                        pagesFieldErrorState = false

                        chapterTitleFieldState = chapterTitle
                        chapterNumberFieldState = chapterNum
                        pageNumberFieldState = pageNum

                        proceedToSaveDialogState.show()
                    }

                }
            )
        }

    }

    MaterialDialog(
        dialogState = resetClockDialogState,
        shape = RoundedCornerShape(10.dp)
    ) {
        MessageDialog(
            message = "You are about to reset the timer. Do you wish to proceed?",
            onDismiss = {
                resetClockDialogState.hide()
            },
            onAccept = {
                isStopWatchPaused = false
                isStopWatchRunning = false
                stopWatch.reset()
                resetClockDialogState.hide()
            })
    }

    MaterialDialog(
        dialogState = proceedToSaveDialogState,
        shape = RoundedCornerShape(10.dp)
    ) {
        MessageDialog(
            message = "You are about to save today's log do you wish to proceed?",
            onDismiss = {
                proceedToSaveDialogState.hide()
            },
            onAccept = {
                //proceed to save to db
                isStopWatchRunning = false
                isStopWatchPaused = false
                stopWatch.reset()
                Timber.tag("TrackBookScreen")
                    .d("Final time from timer => %s", stopWatch.finalTime.toString())
                showTimerAndTrackCard = false
                //log to database
                bookTrackingViewModel.logProgress(
                    bookId = bookId!!,
                    timeTaken = stopWatch.finalTime,
                    chapterTitle = chapterTitleFieldState,
                    currentPage = pageNumberFieldState.toInt(),
                    chapterNumber = chapterNumberFieldState.toInt()
                )

                finishSessionDialogState.hide()
                proceedToSaveDialogState.hide()
            }
        )
    }

}

@Composable
fun AddToReadingListDialog(
    readingList: List<ReadingListUiItem> = listOf(),
    onDismiss: () -> Unit = {},
    onListSelected: (String) -> Unit = {},
    createNewPlayList: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(4f),
                        text = "Save To Reading List",
                        style = BookAppTypography.labelLarge,
                        textAlign = TextAlign.Start
                    )
                    TextButton(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = { createNewPlayList() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "create",
                            style = BookAppTypography.labelMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary
                        )

                    }
                }

                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (readingList.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "No list available",
                                style = BookAppTypography.labelSmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    items(
                        items = readingList,
                        key = {
                            it.id
                        }
                    ) {
                        ListItem(
                            readingListUiItem = it,
                            onItemChecked = { id ->
                                onListSelected(id)
                            }
                        )
                    }
                }
                
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    TextButton(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Done",
                            style = BookAppTypography.labelMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary
                        )

                    }
                }

            }

        }


    }

}

@Composable
fun ListItem(
    readingListUiItem: ReadingListUiItem = ReadingListUiItem(),
    onItemChecked: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier.clickable(
            onClick = {
                onItemChecked(readingListUiItem.id)
            }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = readingListUiItem.isInList,
            onCheckedChange = {},
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary
            ) ,
            enabled = false
        )

        Text(
            modifier = Modifier.weight(5f),
            text = readingListUiItem.name,
            style = BookAppTypography.bodyMedium,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.secondary
        )

    }
}

@Composable
fun MessageDialog(
    message: String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(4.dp),
            text = message,
            textAlign = TextAlign.Center,
            style = BookAppTypography.labelMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ElevatedButton(
                onClick = {
                    onDismiss()
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.wrapContentWidth(),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("cancel", style = BookAppTypography.labelSmall)
            }

            // Spacer(modifier = Modifier.width(5.dp))

            ElevatedButton(
                onClick = {
                    onAccept()
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.wrapContentWidth(),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("proceed", style = BookAppTypography.labelMedium)
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun ProgressGraphSection(
    bookLogs: Map<String, Long> = mapOf(),
    totalTimeSpentWeekly: Long = 0L,
    targetTime: Long = 0L
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Text(
            "History", style = BookAppTypography.headlineSmall, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Row(modifier = Modifier.padding(8.dp)) {
            HoursWithEmojiComponent(
                modifier = Modifier.size(90.dp),
                totalTimeSpentWeekly = totalTimeSpentWeekly

            )
            BarGraph(
                height = 150.dp,
                graphBarData = bookLogs,
                targetDuration = targetTime

            )

        }
    }

}

@Composable
@Preview(showBackground = true)
fun BookProgressSection(
    bookData: BookProgressData = BookProgressData(),
    timerValue: String = "00:00:00",
    showTimerText: Boolean = false,
    isBookActive: Boolean = false,
    timerRunning: Boolean = false,
    isTimerPaused: Boolean = false,
    onShowTimerText: (Boolean) -> Unit = {},
    onFinish: () -> Unit = {},
    onReset: () -> Unit = {}
) {
    val constraints = ConstraintSet {
        val imageSet = createRefFor("progress_image")
        val titleSet = createRefFor("book_title")
        val authorSet = createRefFor("author_title")
        val chapterSet = createRefFor("chapter_title")
        val timerSet = createRefFor("timer")
        val startBtnSet = createRefFor("start_button")
        val finishButton = createRefFor("finish_button")
        val resetTimerBtn = createRefFor("reset_button")

        constrain(imageSet) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(titleSet) {
            top.linkTo(imageSet.bottom, margin = 8.dp)
            start.linkTo(imageSet.start)
            end.linkTo(imageSet.end)
        }
        constrain(authorSet) {
            top.linkTo(titleSet.bottom)
            start.linkTo(titleSet.start)
            end.linkTo(titleSet.end)
        }
        constrain(chapterSet) {
            top.linkTo(authorSet.bottom)
            start.linkTo(authorSet.start)
            end.linkTo(authorSet.end)
        }


        constrain(timerSet) {
            top.linkTo(chapterSet.bottom, 8.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(startBtnSet) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            if (showTimerText) top.linkTo(timerSet.bottom, 8.dp) else top.linkTo(
                chapterSet.bottom,
                8.dp
            )
        }

        if (showTimerText) {
            constrain(resetTimerBtn) {
                start.linkTo(startBtnSet.end)
                top.linkTo(startBtnSet.top)
                bottom.linkTo(startBtnSet.bottom)
            }
            constrain(finishButton) {
                start.linkTo(resetTimerBtn.end)
                top.linkTo(resetTimerBtn.top)
                bottom.linkTo(resetTimerBtn.bottom)
            }
            createHorizontalChain(startBtnSet, resetTimerBtn, finishButton)
        }

    }

    Timber.tag("BookProgressSection").d(bookData.toString())
    // Toast.makeText( LocalContext.current ,bookData.authors , Toast.LENGTH_LONG).show()

    ConstraintLayout(
        constraintSet = constraints,
        modifier = Modifier.fillMaxWidth()
    ) {

        BookProgressImageSection(
            modifier = Modifier.layoutId("progress_image"),
            bookData = bookData
        )

        Text(
            text = bookData.bookTitle,
            style = BookAppTypography.labelLarge,
            modifier = Modifier.layoutId("book_title"),
            fontSize = 20.sp
        )
        Text(
            text = bookData.authors,
            style = BookAppTypography.bodyMedium,
            modifier = Modifier.layoutId("author_title"),
            fontSize = 15.sp
        )

        if (bookData.currentChapter > 0) {
            Row(modifier = Modifier.layoutId("chapter_title")) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark_24),
                    contentDescription = ""
                )
                Text(
                    text = "chapter ${bookData.currentChapter}: ${bookData.currentChapterTitle}",
                    style = BookAppTypography.bodySmall,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 2.dp, start = 2.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = showTimerText,
            exit = fadeOut(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + slideOutVertically(
                animationSpec = tween(durationMillis = 200),
                targetOffsetY = { -it / 2 }
            ),
            enter = fadeIn(
                animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
            ) + slideInVertically(
                animationSpec = tween(durationMillis = 200),
                initialOffsetY = { -it / 2 }
            ),
            modifier = Modifier
                .layoutId("timer")
                .fillMaxWidth()
        )
        {
            Text(
                text = timerValue,
                style = BookAppTypography.headlineMedium,
                fontSize = 36.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isBookActive) {
            ElevatedButton(
                modifier = Modifier.layoutId("start_button"),
                onClick = {
                    //will start timer
                    onShowTimerText(true)
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                val buttonText =
                    if (timerRunning) "pause" else if (isTimerPaused) "resume" else "start"
                Text(text = buttonText, style = BookAppTypography.labelLarge)
            }
        }

        if (showTimerText) {

            ElevatedButton(
                modifier = Modifier.layoutId("reset_button"),
                onClick = {
                    // shows finish dialog
                    onReset()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "reset", style = BookAppTypography.labelLarge)
            }

            ElevatedButton(
                modifier = Modifier.layoutId("finish_button"),
                onClick = {
                    // shows finish dialog
                    onFinish()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                val buttonText = "finish"
                Text(text = buttonText, style = BookAppTypography.labelLarge)
            }
        }
    }

}

@Composable
fun ExpandableDescriptionCard(description: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .clickable(
                onClick = { expanded = !expanded }
            ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(6f),
                    text = "Description",
                    style = BookAppTypography.headlineSmall,
                    maxLines = 1
                )

                val icon =
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown

                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = icon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }


            Spacer(modifier = Modifier.height(6.dp))

            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .verticalScroll(
                            state = rememberScrollState()
                        )
                ) {
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        text = description,
                        style = BookAppTypography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun TrackSection(
    canFinishSession: Boolean = false,
    errorInChapterField: Boolean = false,
    errorInPagesField: Boolean = false,
    errorInChapterNumField: Boolean = false,
    onFinishSession: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var chapterTitle by remember {
        mutableStateOf("")
    }
    var currentChapterNumber by remember {
        mutableStateOf("")
    }
    var currentPageNumber by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Log your current progress",
            style = BookAppTypography.labelMedium,
            textAlign = TextAlign.Center
        )
        // CounterSection(title = "Page")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(0.dp),
                        //if error is available , change the color of border to error color
                        color = if (errorInChapterNumField) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                    )
                    .weight(0.5f),
                value = currentChapterNumber,
                onValueChange = {
                    currentChapterNumber = it
                },
                placeholder = {
                    Text(text = "chapter number", style = BookAppTypography.labelMedium)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    errorCursorColor = MaterialTheme.colorScheme.error
                ),
                singleLine = true,
                textStyle = BookAppTypography.bodyMedium,
                shape = RoundedCornerShape(0.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )
            Spacer(modifier = Modifier.width(5.dp))
            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .border(
                        width = 2.dp,
                        shape = RoundedCornerShape(0.dp),
                        //if error is available , change the color of border to error color
                        color = if (errorInPagesField) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                    )
                    .weight(0.5f),
                value = currentPageNumber,
                onValueChange = {
                    currentPageNumber = it
                },
                placeholder = {
                    Text(text = "page number", style = BookAppTypography.labelMedium)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    errorCursorColor = MaterialTheme.colorScheme.error
                ),
                singleLine = true,
                textStyle = BookAppTypography.bodyMedium,
                shape = RoundedCornerShape(0.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

            )
        }

        /*
                    CounterSection(
                        title = "Chapter" ,
                        chapterCount = finalChapterCount ,
                        increaseValue = {
                            finalChapterCount += 1
                        } ,
                        decreaseValue = {
                            if(finalChapterCount > 0){
                                finalChapterCount -= 1
                            }

                        }
                    )*/

        Spacer(modifier = Modifier.height(6.dp))


        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextFieldComponent(
                modifier = Modifier.fillMaxWidth(),
                hint = "Current chapter title",
                text = chapterTitle,
                onTextChanged = {
                    chapterTitle = it
                },
                hasError = errorInChapterField,
                isSingleLine = true
            )

            ElevatedButton(
                onClick = {
                    onFinishSession(chapterTitle, currentChapterNumber, currentPageNumber)
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("finish session", style = BookAppTypography.labelMedium)
            }
        }

    }
}
/*
@Composable
@Preview(showBackground = true)
fun CounterSection(
    title: String = "Title",
    chapterCount: Int = 0,
    increaseValue: () -> Unit = {},
    decreaseValue: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = BookAppTypography.labelMedium)
        CounterButtonsComponent(
            countValue = chapterCount,
            increaseValue = {
                increaseValue()
            },
            decreaseValue = {
                decreaseValue()
            }
        )
    }
}*/

/*@Composable
@Preview(showBackground = true)
fun CounterButtonsComponent(
    countValue: Int = 0,
    increaseValue: () -> Unit = {},
    decreaseValue: () -> Unit = {}
) {
    val constraintSet = ConstraintSet {

        val decreaseBtn = createRefFor("decrease_btn")
        val countText = createRefFor("count_text")
        val increaseBtn = createRefFor("increase_text")

        constrain(increaseBtn) {
            end.linkTo(parent.end, 4.dp)
            top.linkTo(countText.top)
            bottom.linkTo(countText.bottom)
        }
        constrain(countText) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
        constrain(decreaseBtn) {
            top.linkTo(countText.top)
            bottom.linkTo(countText.bottom)
            start.linkTo(parent.start, 4.dp)
        }
        createHorizontalChain(decreaseBtn, countText, increaseBtn, chainStyle = ChainStyle.Spread)
    }
    ConstraintLayout(constraintSet) {
        OutlinedButton(
            modifier = Modifier.layoutId("decrease_btn"),
            onClick = {
                decreaseValue()
            },
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "",
                modifier = Modifier.size(35.dp)
            )
        }
        Text(
            modifier = Modifier
                .layoutId("count_text")
                .padding(start = 6.dp, end = 6.dp),
            text = countValue.toString(),
            style = BookAppTypography.labelMedium,
            fontSize = 24.sp
        )

        OutlinedButton(
            modifier = Modifier.layoutId("increase_text"),
            onClick = {
                increaseValue()
            },
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.size(35.dp)
            )
        }
    }
}*/

@Composable
fun TimeAndPageComponent(
    pagesStatText: String = "",
    timeStatText: String = "",
    pagesPerMinStatText: AnnotatedString
) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            SingleTimeOrPageItem(
                mainText = pagesStatText,
                title = "pages read"
            )

            PageSpeedComponent(
                annotatedText = pagesPerMinStatText,
                title = "speed"
            )
            SingleTimeOrPageItem(
                mainText = timeStatText,
                title = "best time"
            )
        }

    }
}

@Composable
@Preview
fun SingleTimeOrPageItem(
    modifier: Modifier = Modifier,
    mainText: String = "",
    title: String = ""
) {
    Column(
        modifier = modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = mainText,
            style = BookAppTypography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        Text(
            text = title.uppercase(),
            style = BookAppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            textAlign = TextAlign.Start
        )

    }
}

@Composable
fun PageSpeedComponent(
    modifier: Modifier = Modifier,
    annotatedText: AnnotatedString,
    title: String = ""
) {
    Column(
        modifier = modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = annotatedText,
            style = BookAppTypography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        Text(
            text = title.uppercase(),
            style = BookAppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HoursWithEmojiComponent(
    modifier: Modifier = Modifier,
    totalTimeSpentWeekly: Long = 0L
) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = modifier) {
        Text(
            text = "This week",
            style = BookAppTypography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Text(
            text = totalTimeSpentWeekly.formatTimeToDHMS().splitToDHMS(),
            style = BookAppTypography.labelLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .width(70.dp)
                .height(70.dp)
        ) {
            val resource = when (totalTimeSpentWeekly) {
                in 0L..3600000L -> {
                    R.drawable.shocked_emoji
                }

                in 3600001L..14400000L -> {
                    R.drawable.worried_emoji
                }

                else -> {
                    R.drawable.happy_emoji
                }
            }
            Image(
                painter = painterResource(id = resource),
                contentDescription = "",
                modifier = Modifier.size(60.dp)
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun BookProgressImageSection(
    modifier: Modifier = Modifier,
    bookData: BookProgressData = BookProgressData(),
) {
    var animationTriggered by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true) {
        animationTriggered = true
    }
    val finalProgress = animateFloatAsState(
        targetValue = if (animationTriggered) bookData.progress else 0f,
        label = "book_final_progress",
        animationSpec = tween(delayMillis = 500)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(230.dp)
            .width(230.dp)
            .padding(bottom = 8.dp)
    ) {


        val coilImage = rememberAsyncImagePainter(
            ImageRequest
                .Builder(LocalContext.current)
                .data(data = bookData.bookImage)
                .apply(block = {
                    //placeholder(R.drawable.image_placeholder_24)
                    //error(R.drawable.ic_error_24)
                    crossfade(true)
                    transformations(
                        RoundedCornersTransformation(0f)
                    )
                }).build()
        )

        val painterState = coilImage.state

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = coilImage,
                contentDescription = "",
                // .clip(RoundedCornerShape(5.dp)) ,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(135.dp)
                    .width(100.dp)
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                modifier = Modifier.wrapContentWidth(),
                text = "${(bookData.progress * 100).toInt()}%",
                textAlign = TextAlign.Center,
                style = BookAppTypography.headlineMedium
            )
        }
        //glide image

        if (painterState is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }


        CircularProgressIndicator(
            progress = finalProgress.value,
            strokeCap = StrokeCap.Round,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(width = 250.dp, height = 250.dp),
            strokeWidth = 12.dp,
            trackColor = MaterialTheme.colorScheme.onBackground
        )
    }
}
