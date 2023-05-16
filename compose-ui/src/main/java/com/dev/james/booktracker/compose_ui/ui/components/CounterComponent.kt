package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@Composable
@Preview(name = "CounterComponent" , showBackground = true)
fun CounterComponent(
    onSet : (Int) -> Unit = {} ,
    onDismiss : () -> Unit = {}
){
    var count by remember {
        mutableStateOf(0)
    }

    var countExceeded by remember {
        mutableStateOf(false)
    }

    Surface(
        shape = RoundedCornerShape(15.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(8.dp)
        ) {

            CounterSelector(
                onUpClicked = {
                    count += 1
                } ,
                onDownClicked = {
                    if(count > 0){
                        count -= 1
                    }
                } ,
                selectedNumber = count
            )


            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RoundedBrownButton(
                    label = "close",
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        onDismiss()
                    }
                )

                RoundedBrownButton(
                    label = "set",
                    onClick = {
                        //set selected time
                        onSet(
                            count
                        )
                        onDismiss()
                    }
                )

            }
        }


    }

}

@Composable
fun CounterSelector(
    selectedNumber : Int = 0 ,
    hasError : Boolean = false ,
    onUpClicked : () -> Unit = {} ,
    onDownClicked : () -> Unit = {}
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        IconButton(
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color.Transparent
            ),
            onClick = {
                onUpClicked()
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Up arrow for",
                tint = MaterialTheme.colorScheme.secondary
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Text(
                text = selectedNumber.toString(),
                style = BookAppTypography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 52.sp
                //modifier = Modifier.fillMaxWidth()
            )
        }

        IconButton(
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color.Transparent
            ),
            onClick = {
                onDownClicked()
            }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Down arrow",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}