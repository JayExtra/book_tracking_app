package com.dev.james.booktracker.on_boarding.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.theme.Brown

@Composable
fun StatefulRoundOutlineButton(
    text: String,
    backgroundColor : Color ,
    outlineColor : Color ,
    textColor : Color ,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .border(width = 2.dp, color = outlineColor, shape = RoundedCornerShape(10.dp))
            .width(intrinsicSize = IntrinsicSize.Max)
            .height(40.dp)
            .background(color = backgroundColor)
            .clickable {
                  onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.lowercase(),
            style = BookAppTypography.body1,
            color = textColor ,
            modifier = Modifier.padding(bottom = 5.dp , start = 30.dp , end = 30.dp)
        )
    }

}

@Composable
@Preview
fun StatelessRoundOutlineButton(
    modifier: Modifier = Modifier,
    text: String = "Next",
    backgroundColor : Color = Brown,
    textColor: Color = Color.White,
    outlineColor : Color = Color.White,
) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .border(width = 2.dp, color = outlineColor, shape = RoundedCornerShape(10.dp))
            .width(intrinsicSize = IntrinsicSize.Max)
            .height(30.dp)
            .background(color = backgroundColor) ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.lowercase(),
            style = BookAppTypography.body2,
            color = textColor ,
            modifier = Modifier.padding(bottom = 5.dp , start = 20.dp , end = 20.dp)
        )
    }

}