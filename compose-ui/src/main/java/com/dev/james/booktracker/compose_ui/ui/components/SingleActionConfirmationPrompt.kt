package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleActionConfirmationPrompt(
    modifier : Modifier = Modifier,
    title : String,
    body : String,
    onAccept : () -> Unit = {},
    onCancel : () -> Unit = {},
    showAnimation : Boolean = true,
    confirmationText : String = stringResource(R.string.confirm)
){
    val isAnimationPlaying by rememberSaveable {
        mutableStateOf(true)
    }
    val speed by rememberSaveable {
        mutableStateOf(1f)
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onCancel() }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth() ,
            tonalElevation = AlertDialogDefaults.TonalElevation ,
            color = MaterialTheme.colorScheme.background ,
            shape = RoundedCornerShape(12.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp) ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(showAnimation){
                    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.success_lottie))
                    val progress by animateLottieCompositionAsState(
                        composition = composition ,
                        iterations = 1,
                        isPlaying = isAnimationPlaying ,
                        speed = speed ,
                        restartOnPlay = false
                    )
                    LottieAnimation(
                        modifier = Modifier.width(70.dp).height(70.dp),
                        composition = composition ,
                        progress = progress ,
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = title ,
                    style = BookAppTypography.headlineMedium ,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = body ,
                    style = BookAppTypography.bodyMedium ,
                    textAlign = TextAlign.Center
                )

                ElevatedButton(
                    modifier = Modifier.fillMaxWidth() ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = { onAccept() }
                )
                {
                    Text(text = confirmationText)
                }

            }

        }
    }

}

/*
@PreviewLightDark
@Composable
fun SingleActionConfirmationDialog(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.0f)
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp)
    ){
        SingleActionConfirmationPrompt(
            title = "Test title" ,
            body = "This is some test body innit! Have a good one" ,
            onAccept = {} ,
            onCancel = {}
        )
    }
}*/
