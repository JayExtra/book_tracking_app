package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@Composable
@Preview(name = "RowDateSelectorComponent" , showBackground = true)
fun WeekDaySelectorComponent(
    modifier : Modifier = Modifier ,
    hasError : Boolean = false ,
    selectedDays : List<String> = listOf() ,
    onDaySelected : (String) -> Unit = {}
){
    Column(
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = modifier

    ) {

        Text(
            text = "Select specific days" ,
            modifier = Modifier.fillMaxWidth(),
            style = BookAppTypography.labelMedium ,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        val dayList = listOf("Sun" , "Mon" , "Teu" , "Wen" , "Thur" , "Fri" , "Sat")
        LazyRow(
            modifier = modifier ,
            contentPadding = PaddingValues(8.dp) ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(dayList.size){
                DayItem(
                    dayString = dayList[it] ,
                    isSelected = selectedDays.contains(dayList[it]) ,
                    itemSelected = { day ->
                        onDaySelected(day)
                    } ,
                    hasError = hasError
                )
            }
        }
    }

}

@Composable
fun DayItem(
    dayString : String = "" ,
    hasError: Boolean = false ,
    isSelected : Boolean = false ,
    itemSelected : (String) -> Unit = {}
){
    val color = if(!isSelected && hasError){
        MaterialTheme.colorScheme.error
    }else{
        MaterialTheme.colorScheme.primary
    }
    Box(
        contentAlignment = Alignment.Center ,
        modifier = Modifier
            .size(45.dp)
            .clip(shape = CircleShape)
            .border(
                width = if (isSelected) 0.dp else 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.secondary else color ,
                shape = CircleShape
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
            )
            .clickable {
                itemSelected(dayString)
            }
    ) {

        Text(
            text = dayString ,
            color = if(isSelected) MaterialTheme.colorScheme.onPrimary else color ,
            style = BookAppTypography.bodySmall
        )
        
    }

}