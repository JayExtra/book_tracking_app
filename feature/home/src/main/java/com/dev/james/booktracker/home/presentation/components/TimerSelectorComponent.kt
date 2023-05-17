package com.dev.james.booktracker.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.james.booktracker.compose_ui.ui.components.RoundedBrownButton
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@Composable
@Preview("TimerSelectorComponent", showBackground = true)
fun TimerSelectorComponent(
    onSet: (SelectedTime) -> Unit = {
        SelectedTime(
            0 , 0
        )
    },
    onDismiss: () -> Unit = {}
) {

    var selectedHour by remember {
        mutableStateOf(0)
    }

    var selectedMinutes by remember {
        mutableStateOf(0)
    }

    var hasExceededHours by remember {
        mutableStateOf(false)
    }

    var hasExceededMinutes by remember {
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                HourOrMinuteComponent(
                    selectedNumber = selectedHour,
                    onUpClicked = {
                        if (selectedHour < 24) {
                            selectedHour += 1
                        } else {
                            hasExceededHours = true
                        }
                    },

                    onDownClicked = {
                        if (selectedHour > 0) {
                            hasExceededHours = false
                            selectedHour -= 1
                        }
                    },
                    hasError = hasExceededHours
                )

                Text(
                    text = "h",
                    style = BookAppTypography.headlineMedium,
                    color = if (hasExceededHours) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.width(17.dp))


                Text(
                    text = ":",
                    style = BookAppTypography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.width(15.dp))


                HourOrMinuteComponent(
                    selectedNumber = selectedMinutes,
                    onUpClicked = {
                        if (selectedMinutes != 55) {
                            hasExceededMinutes = false
                            selectedMinutes += 5
                        }else {
                            selectedMinutes = 0
                            selectedHour += 1
                        }
                    },

                    onDownClicked = {
                        if (selectedMinutes > 0) {
                            hasExceededMinutes = false
                            selectedMinutes -= 5
                        }
                    },
                    hasError = hasExceededMinutes
                )


                Text(
                    text = "m",
                    style = BookAppTypography.headlineLarge,
                    color = if (hasExceededMinutes) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )


            }

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
                            SelectedTime(
                                hours = selectedHour ,
                                minutes = selectedMinutes
                            )
                        )
                        onDismiss()
                    }
                )

            }


        }

    }


}


@Composable
@Preview("HourMinuteComponent")
fun HourOrMinuteComponent(
    selectedNumber: Int = 0,
    hasError: Boolean = false,
    onDownClicked: () -> Unit = {},
    onUpClicked: () -> Unit = {}
) {


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
                fontSize = 42.sp
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

data class SelectedTime(
    val hours : Int ,
    val minutes : Int
)