package com.dev.james.booktracker.compose_ui.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography


@Preview
@Composable
fun AddBookOptionsDialog(
    onWishlist : () -> Unit = {},
    onExists : () -> Unit = {} ,
    onDismiss : () -> Unit = {}
){
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OptionPlaque(
                modifier = Modifier.padding(4.dp) ,
                message = "Already have the book? Let's add it to start tracking it!" ,
                iconPainter = R.drawable.book_icon ,
                onSelected = {
                    onExists()
                }
            )

            OptionPlaque(
                modifier = Modifier.padding(4.dp) ,
                message = "Add book to wishlist. Book will be searched online.",
                iconPainter = 0 ,
                iconVector = Icons.Default.Star,
                onSelected = {
                    onWishlist()
                }
            )
        }
    }
}


@Preview
@Composable
fun OptionPlaque(
    modifier: Modifier = Modifier ,
    message : String = "" ,
    iconVector : ImageVector = Icons.Default.Warning,
    iconTint : Color = MaterialTheme.colorScheme.background ,
    @DrawableRes iconPainter : Int = 0 ,
    onSelected : () -> Unit = {},
){
    Surface(
        modifier = modifier
            .height(100.dp)
            .width(150.dp)
            .clickable(
                onClick = {
                    onSelected()
                }
            ),
        shape = RoundedCornerShape(10.dp) ,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp) ,
            verticalArrangement = Arrangement.Top ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.weight(0.5f),
                text = message ,
                style = BookAppTypography.bodySmall ,
                textAlign = TextAlign.Start,
                fontSize = 11.sp ,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight() ,
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(35.dp),
                    shape = CircleShape  ,
                    color = MaterialTheme.colorScheme.onBackground
                ) {
                    Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
                        if(iconPainter != 0){
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = iconPainter) ,
                                contentDescription = "",
                                tint = iconTint
                            )
                        }else {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = iconVector ,
                                contentDescription = "",
                                tint = iconTint
                            )
                        }

                    }
                }
            }
        }
    }
}