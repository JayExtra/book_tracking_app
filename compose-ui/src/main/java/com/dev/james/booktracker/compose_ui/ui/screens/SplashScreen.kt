package com.dev.james.booktracker.compose_ui.ui.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.theme.Brown
import com.dev.james.booktracker.compose_ui.ui.theme.GrayHue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    doneLoading : () -> Unit
) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(3000L)
        doneLoading()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Brown),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogo(scale)
        Text(
            text = "Book Tracker" ,
            color = Color.White ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .scale(scale.value),
            style = MaterialTheme.typography.h6 ,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun AppLogo(scale: Animatable<Float, AnimationVector1D>) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .scale(scale.value),
        contentAlignment = Alignment.Center
    ) {
        ColorBorderComponent()
        Image(
            painter = painterResource(id = R.drawable.book_icon_outline),
            contentDescription = stringResource(id = R.string.app_logo_desc)
        )
    }
}


@Composable
@Preview
fun ColorBorderComponent(modifier: Modifier = Modifier) {
    Row {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .weight(weight = 1f)
                .background(color = GrayHue.copy(alpha = 0.5f))
        )
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .weight(1f)
                .background(color = Color.White)
        )

    }
}