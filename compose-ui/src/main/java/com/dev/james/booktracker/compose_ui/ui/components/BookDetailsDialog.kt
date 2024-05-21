package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.BaseBookData

@Preview(showBackground = true)
@Composable
fun BookDetailsDialog(
    book : BaseBookData = BaseBookData(),
    removeFromWishlist : (BaseBookData) -> Unit = {},
    showRemoveBtn : Boolean = true ,
    addBook : (BaseBookData) -> Unit = {},
    onDismiss : () -> Unit = {}
){

    var isExpanded by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp) ,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp) ,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CoilImageComponent(
                        modifier = Modifier
                            .width(90.dp)
                            .height(130.dp) ,
                        image = book.image
                    )

                    Column(
                        modifier = Modifier
                            .weight(5f)
                            .padding(start = 4.dp),
                        horizontalAlignment = Alignment.Start ,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ){
                        Text(
                            text = book.title,
                            style = BookAppTypography.labelLarge
                        )
                        Text(
                            text = book.authors ,
                            style = BookAppTypography.bodySmall
                        )
                        Text(
                            text = book.publisher ,
                            style = BookAppTypography.bodySmall
                        )
                        Text(
                            text = book.publishedDate ,
                            style = BookAppTypography.bodySmall
                        )
                        Text(
                            text = "${book.pages} pages" ,
                            style = BookAppTypography.bodySmall
                        )

                        Row(
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            if(showRemoveBtn){
                                IconButton(
                                    modifier = Modifier.clip(
                                        CircleShape
                                    ),
                                    colors = IconButtonDefaults
                                        .iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary
                                        ),
                                    onClick = { removeFromWishlist(book) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "" ,
                                        tint = MaterialTheme.colorScheme.background
                                    )
                                }
                            }

                            IconButton(
                                modifier = Modifier.clip(
                                    CircleShape
                                ),
                                colors = IconButtonDefaults
                                    .iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
                                    ),
                                onClick = { addBook(book) }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_save_24),
                                    contentDescription = "" ,
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        }


                    }
                }

                Divider(
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    modifier = Modifier.fillMaxWidth() ,
                    text = "About book" ,
                    style = BookAppTypography.headlineSmall ,
                    textAlign = TextAlign.Start
                )

                if(book.category.isNotEmpty()){
                   // Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth()){
                        for(category in book.category){
                            TagComponent(
                                name = category
                            )
                        }
                    }
                }

                val height = if (isExpanded) 155.dp else 150.dp

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .clickable(
                            onClick = {
                                isExpanded = !isExpanded
                            }
                        )
                        .verticalScroll(
                            state = rememberScrollState(),
                            enabled = isExpanded
                        )
                ) {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = book.description ,
                        style = BookAppTypography.bodyMedium ,
                        maxLines = if(isExpanded) Int.MAX_VALUE else 8 ,
                        overflow = if(isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis ,
                        textAlign = TextAlign.Justify
                    )
                }

                Divider(
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth() ,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text(
                            text = "Dismiss" ,
                            style = BookAppTypography.labelMedium
                        )
                    }
                }
                if(isExpanded){
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookChaptersDialog(
    onSave : (Int) -> Unit = {} ,
    onDismiss: () -> Unit = {}
){
    var pages by remember {
        mutableStateOf("")
    }
    var pagesErrorState by remember {
        mutableStateOf(false)
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp) ,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp) ,
                verticalArrangement = Arrangement.spacedBy(6.dp) ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.width(250.dp) ,
                    text = "Please provide the chapter count in this book" ,
                    style = BookAppTypography.headlineSmall ,
                    textAlign = TextAlign.Center
                )

                OutlinedTextFieldComponent(
                    modifier = Modifier.fillMaxWidth() ,
                    text = pages ,
                    hasError = pagesErrorState ,
                    label = "Total Chapters" ,
                    roundedCornerSize = 10.dp ,
                    keyBoardType = KeyboardType.Number ,
                    onTextChanged = { text ->
                        pages = text
                    }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if(pages.isBlank()){
                            pagesErrorState = true
                        }else {
                            pagesErrorState = false
                            onSave(pages.toInt())
                        }
                } ,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = "save book" , style = BookAppTypography.labelMedium)
                }
            }
        }
    }

}
