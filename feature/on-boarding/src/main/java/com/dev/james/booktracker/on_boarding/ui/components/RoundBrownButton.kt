package com.dev.james.booktracker.on_boarding.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.dev.james.booktracker.compose_ui.ui.theme.Orange

@Composable
fun RoundedBrownButton(
    modifier: Modifier = Modifier ,
    text : String  ,
    onClick : () -> Unit
){
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(25.dp)
            )
            .width(intrinsicSize = IntrinsicSize.Max)
            .height(40.dp)
            .background(color = Orange)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = BookAppTypography.body1,
            color = Color.White ,
            modifier = Modifier.padding(bottom = 5.dp , start = 35.dp , end = 35.dp)
        )
    }
}
