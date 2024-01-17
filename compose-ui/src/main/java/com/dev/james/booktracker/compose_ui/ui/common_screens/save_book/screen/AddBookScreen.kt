package com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.screen

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.forms.CurrentReadForm
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.navigation.AddBookScreenNavigator
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.AddBookViewModel
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.CurrentReadFormActions
import com.dev.james.booktracker.compose_ui.ui.common_screens.save_book.viewmodel.ImageSelectorUiState
import com.dev.james.booktracker.compose_ui.ui.components.CameraView
import com.dev.james.booktracker.compose_ui.ui.components.GoogleBooksSearchBottomSheet
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dsc.form_builder.FormState
import com.dsc.form_builder.TextFieldState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private lateinit var outputDirectory: File
private lateinit var cameraExecutor: ExecutorService
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun AddBookScreen(
    addBookScreenNavigator: AddBookScreenNavigator ,
    addBookViewModel: AddBookViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val imageSelectorState = addBookViewModel.imageSelectorUiState.collectAsStateWithLifecycle()
    val currentReadFormState = addBookViewModel.currentReadFormState

    var shouldShowCameraScreen by remember { mutableStateOf(false) }

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


    if (shouldShowCameraScreen) {
        CameraView(
            outputDirectory = outputDirectory,
            executor = cameraExecutor,
            onImageCaptured = { uri ->
                //update the image state in view model
                addBookViewModel.passUiAction(action = CurrentReadFormActions.ImageSelected(imageUri = uri))
                //hide camera
                shouldShowCameraScreen = false

            },
            onError = { error ->
                Toast.makeText(context, "${error.message}", Toast.LENGTH_SHORT).show()
            },
            onCloseAction = {
                shouldShowCameraScreen = false

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
                                //readGoalsScreenViewModel.cancelQueryJob()
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

            StatelessAddBookScreen(
                context = context,
                currentReadFormState = currentReadFormState,
                imageSelectorState = imageSelectorState.value,
                popBackStack = {

                } ,
                onGoogleIconClicked = {
                    coroutineScope.launch {
                        sheetState.expand()
                    }
                } ,
                onSaveClicked = {

                } ,
                onClearImage = {

                } ,
                onImageSelectorClicked = {

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
                    stringResource(R.string.camera_permission_message_redirect)
                } else {
                    stringResource(R.string.camera_permission_message)
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


@Composable
fun StatelessAddBookScreen(
    modifier: Modifier = Modifier ,
    context: Context = LocalContext.current,
    currentReadFormState : FormState<TextFieldState>,
    imageSelectorState : ImageSelectorUiState ,
    popBackStack: () -> Unit = {},
    onGoogleIconClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onImageSelectorClicked: () -> Unit = {},
    onClearImage: () -> Unit = {},
){

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        StandardToolBar(
            title = {
                    Text(text = "Add a physical book")
            },
            navActions = {
                //control visibility depending on where we are
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

            },
            navigate = {
                //navigate back to home screen
                popBackStack()
            },
        )

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
