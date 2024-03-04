package com.dev.james.my_library.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.enums.DefaultColors
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.compose_ui.ui.utils.getDarkerColor
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.core.common_models.SuggestedBook


@Preview
@Composable
fun SuggestedBookCardComponent(
    cardColor: Long = DefaultColors.DEFAULT_CARD_COLOR,
    book : SuggestedBook = SuggestedBook(),
    onAddToWishlistSelected : (SuggestedBook) -> Unit = {}
) {

    Card(
        modifier = Modifier
            .width(242.dp)
            .height(139.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(cardColor)
        )
        //onClick = {  }
    ) {
        val constraintsSet = ConstraintSet {
            val bookImage = createRefFor("book_image")
            val bookDetails = createRefFor("book_details")
            val wishlistBtn = createRefFor("wishlist_button")


            constrain(bookImage) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }

            constrain(bookDetails) {
                start.linkTo(bookImage.end, 4.dp)
                top.linkTo(bookImage.top)
            }

            constrain(wishlistBtn) {
                top.linkTo(bookDetails.bottom , 4.dp)
                start.linkTo(bookImage.end, 4.dp)

            }
        }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .drawBehind {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val arcRadius = 120.dp
                    val arcRadiusSmall = 85.dp
                    val circleRadius = 25.dp

                    drawArc(
                        size = Size(width = 120f, height = 120f),
                        color = Color(cardColor).getDarkerColor(0.3f),
                        useCenter = false,
                        startAngle = 90f,
                        sweepAngle = 180f,
                        style = Stroke(7.dp.toPx()),
                        topLeft = Offset(
                            canvasWidth / 2 + arcRadius.toPx() / 2,
                            canvasHeight / 2 + arcRadius.toPx() / 8
                        )
                    )

                    drawCircle(
                        color = Color(cardColor).getDarkerColor(0.3f),
                        radius = 27f,
                        center = Offset(
                            canvasWidth - circleRadius.toPx() / 0.8f,
                            canvasHeight - circleRadius.toPx() / 1.0f
                        )
                    )

                    drawArc(
                        size = Size(width = 85f, height = 85f),
                        color = Color(cardColor).getDarkerColor(0.3f),
                        useCenter = false,
                        startAngle = -90f,
                        sweepAngle = 180f,
                        style = Stroke(5.dp.toPx()),
                        topLeft = Offset(
                            canvasWidth - arcRadiusSmall.toPx() / 2.0f,
                            canvasHeight - arcRadiusSmall.toPx() / 2.1f
                        )
                    )
                },
            constraintSet = constraintsSet
        ) {

            CoilImageComponent(
                modifier = Modifier
                    .layoutId("book_image")
                    .width(57.dp)
                    .height(89.dp) ,
                image = book.image
            )
            BookDescriptionTextSection(
                modifier = Modifier
                    .layoutId("book_details")
                    .width(135.dp)
                    .wrapContentHeight() ,
                cardColor = cardColor ,
                title = book.title ,
                publisher = book.publisher ,
                date = book.publishedDate ,
                pages = book.pages ,
                author = book.authors
            )

            Button(
                modifier = Modifier
                    .layoutId("wishlist_button")
                    .width(70.dp)
                    .height(20.dp),
                onClick = { onAddToWishlistSelected(book) } ,
                contentPadding = PaddingValues(2.dp) ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(cardColor).getDarkerColor(0.3f)
                )
            ) {

                Icon(
                    modifier = Modifier
                        .width(9.dp)
                        .height(18.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
                Text(
                    text = "wishlist" ,
                    style = BookAppTypography.labelSmall ,
                    fontSize = 8.sp
                )
            }

        }

    }
}


@Composable
fun BookDescriptionTextSection(
    modifier : Modifier = Modifier,
    cardColor : Long = DefaultColors.DEFAULT_CARD_COLOR,
    title : String = "",
    publisher : String = "",
    date : String = "",
    pages : Int = 0,
    author : String = ""
){
    Column(
        modifier = modifier ,
        verticalArrangement = Arrangement.spacedBy(2.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = title ,
            style = BookAppTypography.labelMedium ,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp ,
            maxLines = 2 ,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = author ,
            color = Color(cardColor).getDarkerColor(0.3f) ,
            style = BookAppTypography.labelMedium ,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp ,
            maxLines = 1 ,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "$publisher , $date" ,
            color = Color(cardColor).getDarkerColor(0.3f) ,
            style = BookAppTypography.labelMedium ,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp ,
            maxLines = 1 ,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.fillMaxWidth() ,
            text = "$pages pgs",
            style = BookAppTypography.labelSmall ,
            fontWeight = FontWeight.Bold,
            fontSize = 8.sp
        )
    }
}
