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
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(10.dp))
            .width(intrinsicSize = IntrinsicSize.Max)
            .height(30.dp)
            .background(color = Brown)
            .clickable {
                  onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.lowercase(),
            style = BookAppTypography.body2,
            color = Color.White ,
            modifier = Modifier.padding(bottom = 5.dp , start = 20.dp , end = 20.dp)
        )
    }

}

@Composable
@Preview
fun StatelessRoundOutlineButton(
    modifier: Modifier = Modifier,
    text: String = "Next"
) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(10.dp))
            .width(intrinsicSize = IntrinsicSize.Max)
            .height(30.dp)
            .background(color = Brown) ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.lowercase(),
            style = BookAppTypography.body2,
            color = Color.White ,
            modifier = Modifier.padding(bottom = 5.dp , start = 20.dp , end = 20.dp)
        )
    }

}