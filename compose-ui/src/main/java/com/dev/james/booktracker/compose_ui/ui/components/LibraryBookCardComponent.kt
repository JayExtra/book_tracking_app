package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.LibraryBookData

@Preview(showBackground = true)
@Composable
fun LibraryBookCardComponent(
    modifier : Modifier = Modifier ,
    book : LibraryBookData = LibraryBookData() ,
    onItemSelected : () -> Unit = {}
){
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .clickable {
                 onItemSelected()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ){
        CoilImageComponent(
            modifier = Modifier
                .width(104.dp)
                .height(144.dp) ,
            image = book.image
        )
        Text(
            modifier = Modifier.width(104.dp),
            text = book.title ,
            style = BookAppTypography.labelSmall ,
            fontSize = 13.sp ,
            textAlign = TextAlign.Start ,
            maxLines = 2,
            softWrap = true,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.width(104.dp),
            text = book.author ,
            style = BookAppTypography.labelMedium ,
            fontSize = 10.sp ,
            textAlign = TextAlign.Start ,
            maxLines = 1,
            color = MaterialTheme.colorScheme.primary ,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${book.progress}%" ,
            style = BookAppTypography.labelMedium ,
            fontSize = 16.sp ,
            textAlign = TextAlign.Start ,
            color = MaterialTheme.colorScheme.secondary
        )
    }

}