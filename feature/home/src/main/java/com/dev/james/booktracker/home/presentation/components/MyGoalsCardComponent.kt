package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
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
@Preview(showBackground = false)
fun MyGoalsCardComponent() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp) ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ) ,
        shape = RoundedCornerShape(15.dp) ,
        elevation = CardDefaults.cardElevation(5.dp)

    ) {
        Spacer(modifier = Modifier.height(6.dp))

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
                .padding(6.dp)
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_lightning_24) ,
                tint = MaterialTheme.colorScheme.primary ,
                contentDescription = ""
            )

            Text(
                text = "Read 3 hrs a day accept Saturday and Sunday" ,
                style = BookAppTypography.bodySmall ,
                modifier = Modifier.padding(start = 8.dp , top = 2.dp)
            )

        }

        Spacer(modifier = Modifier.height(6.dp))

        BarGraph(
            graphBarData = mapOf(
                "Sun" to 7200000L , "Mon" to 3600000L , "Teu" to 1800000L , "Wen" to 1200000L , "Thur" to 3600000L , "Fri" to 2400000L , "Sat" to 600000L
            ) ,
            height = 270.dp ,
            targetDuration = 7200000L
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
            .padding(start = 6.dp, end = 6.dp),
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