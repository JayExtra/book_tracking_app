package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography


@Preview(showBackground = true)
@Composable
fun TagComponent(
    modifier : Modifier = Modifier,
    name : String = "",
    onSelected : (String) -> Unit = {}
){
   var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true){
        startAnimation = true
    }

    val rotation = animateFloatAsState(
        targetValue = if(startAnimation) 360f else 0f ,
        animationSpec = tween(
            durationMillis = 1000 ,
            easing = LinearEasing
        ), label = ""
    )

    Surface(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            /*.clickable(
                onClick = {
                    onSelected(name)
                }
            )*/,
        shape = RoundedCornerShape(15.dp) ,
        color = MaterialTheme.colorScheme.secondary
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                modifier = Modifier
                    .rotate(
                        rotation.value
                    ),
                painter = painterResource(id = R.drawable.round_pentagon_tag_24),
                contentDescription = "" ,
                tint = MaterialTheme.colorScheme.background
            )
            Text(
               // modifier = Modifier.weight(5f),
                text = name ,
                style = BookAppTypography.labelSmall ,
                textAlign = TextAlign.Start
            )
        }
    }


}