package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.R


@Composable
fun SelectableChip(
    modifier: Modifier = Modifier ,
    isChipSelected : Boolean = false ,
    chipIsSelected : (String) -> Unit ,
    text : String
){

    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(25.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(25.dp)
            )
           // .padding(8.dp)
            .width(intrinsicSize = IntrinsicSize.Max)
            .background(color = if (isChipSelected) MaterialTheme.colorScheme.onPrimary else Color.Transparent)
            .clickable {
                chipIsSelected(text)
            },
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = text.lowercase(),
            style = BookAppTypography.displayMedium,
            color = if (isChipSelected) Color.White else MaterialTheme.colorScheme.onPrimary ,
            modifier = Modifier.padding(10.dp)
        )

        if (isChipSelected){
            Icon(
                painter = painterResource(id = R.drawable.outline_check_circle_24),
                contentDescription = "Check mark" ,
                tint = Color.White ,
                modifier = Modifier.padding(8.dp)
            )
        }
    }


}