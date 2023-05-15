package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@Composable
@Preview
fun RoundedBrownButton(
    modifier: Modifier = Modifier,
    label: String = "some text",
    cornerRadius: Dp = 15.dp,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    color: Color = MaterialTheme.colorScheme.secondary,
    icon: Int = 0,
    onClick: () -> Unit = {}
) {

    ElevatedButton(
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max)
            .height(40.dp),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        onClick = {
            onClick()
        },
        contentPadding = PaddingValues(bottom = 8.dp, start = 35.dp, end = 35.dp , top = 8.dp)
    ) {
        if (icon > 0) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon for $label"
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = BookAppTypography.labelLarge,
            color = textColor
        )
    }
}
