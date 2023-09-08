package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.dev.james.booktracker.compose_ui.ui.components.BarGraph
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R

@Composable
@Preview(showBackground = true)
fun MyGoalsCardComponent() {
    Card(
        modifier = Modifier.fillMaxWidth() ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ) ,
        shape = RoundedCornerShape(15.dp) ,

    ) {
        TitleAndEditButtonSection()
        Text(
            text = "overall" ,
            style = BookAppTypography.labelMedium ,
            modifier = Modifier.fillMaxWidth() ,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_lightning_24) ,
                tint = MaterialTheme.colorScheme.primary ,
                contentDescription = ""
            )

            Text(
                text = "Read 3 hrs a day accept Saturday and Sunday" ,
                style = BookAppTypography.bodySmall ,
                modifier = Modifier.padding(start = 8.dp)
            )

        }

        BarGraph(
            graphBarData = mapOf(
                "Sun" to 0.55f , "Mon" to 0.33f , "Teu" to 0.667f , "Wen" to 0.222f , "Thur" to 0.345f , "Fri" to 0.256f , "Sat" to 0.1234f
            ) ,
            height = 270.dp ,
            _targetTime = 3
        )

    }

}

@Composable
@Preview(showBackground = true)
fun TitleAndEditButtonSection(
    navigateToEdit : () -> Unit = {}
) {
    val constraints = ConstraintSet {
        val titleText = createRefFor("title_text")
        val editButton = createRefFor("edit_button")
        constrain(titleText) {
            start.linkTo(parent.start , 4.dp)
            top.linkTo(parent.top , 4.dp)
        }
        constrain(editButton) {
            top.linkTo(titleText.top)
            bottom.linkTo(titleText.bottom)
            end.linkTo(parent.end)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        constraintSet = constraints
    ) {
        Text(
            modifier = Modifier.layoutId("title_text"),
            text = "My goals",
            style = BookAppTypography.labelLarge
        )
        TextButton(
            modifier = Modifier.layoutId("edit_button"),
            contentPadding = PaddingValues(4.dp),
            onClick = {
                //handle navigation to goal details fragment
                navigateToEdit()
            }) {
            Text(text = "edit" , style = BookAppTypography.labelMedium)
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "" ,
                modifier = Modifier.padding(start = 4.dp) ,
                tint = MaterialTheme.colorScheme.primary
            )
        }

    }
}