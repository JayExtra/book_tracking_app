package com.dev.james.booktracker.home.presentation.screens

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppShapes
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.navigation.HomeNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun ReadGoalScreen(
    homeNavigator: HomeNavigator
){

    StatelessReadGoalScreen(
        popBackStack = {
            homeNavigator.openHomeScreen()
        }
    )
}

@Composable
@Preview(name = "ReadGoalScreen" , showBackground = true)
fun StatelessReadGoalScreen(
    popBackStack : () -> Unit = {} ,
    onGoogleIconClicked : () -> Unit  = {}
){
  Column(
      verticalArrangement = Arrangement.Top ,
      horizontalAlignment = Alignment.CenterHorizontally ,
      modifier = Modifier.fillMaxSize()
  ){
      StandardToolBar(
          navActions = {
              //control visibility depending on where we are
              IconButton(
                  onClick = {
                      onGoogleIconClicked()
                  } ,
                  colors = IconButtonDefaults.iconButtonColors(
                      containerColor = MaterialTheme.colorScheme.secondaryContainer
                  ) ,
                  modifier = Modifier.clip(shape = CircleShape) ,
              ){
                  Icon(
                      painter = painterResource(id = R.drawable.google_icon_24),
                      contentDescription = "google icon for searching books",
                      tint = MaterialTheme.colorScheme.secondary
                  )
              }
          } ,
          navigate = {
              //navigate back to home screen
              popBackStack()
          }
      ) {
          Text(
              text = "Add your current read",
              style = BookAppTypography.headlineSmall,
              color = MaterialTheme.colorScheme.secondary
          )
      }

      CurrentReadForm()



  }
}

@Composable
@Preview("CurrentReadForm")
fun CurrentReadForm(
    //will take in form state
    onSaveBookClicked : () -> Unit = {}
){
    Column(
        verticalArrangement = Arrangement.Top ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = Modifier.padding(16.dp)
    ) {

        ImageSelectorComponent()

        Spacer(modifier = Modifier.height(32.dp))

        TextFieldComponent(
            label = "Title"
        )
        Spacer(modifier = Modifier
            .height(16.dp)
            .padding(start = 16.dp))

        TextFieldComponent(
            label = "Author"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            DropDownComponent(
                label = "Chapters" ,
                dropDownItems = listOf<String>("1" , "2" , "3")
            )

           Spacer(modifier = Modifier.width(30.dp))

            DropDownComponent(
                label = "Current chapter" ,
                dropDownItems = listOf<String>("1" , "2" , "3")
            )


        }

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldComponent(
            label = "Current chapter title"
        )

        Spacer(modifier = Modifier.height(40.dp))

        ElevatedButton(
            modifier = Modifier.fillMaxWidth() ,
            onClick = { } ,
            shape = RoundedCornerShape(10.dp) ,
            contentPadding = PaddingValues(16.dp) ,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(
                text = "Add book" ,
                style = BookAppTypography.labelLarge,
                textAlign = TextAlign.Center ,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

    }

}

@Composable
@Preview("ImageSelectorComponent")
fun ImageSelectorComponent(
    showProgressBar : Boolean = false ,
    showPlaceHolder : Boolean = true ,
    //could be image could be uri , subject to change
    selectedImage : String = "" ,
    onSelect : () -> Unit = {}
){
    Box(
        contentAlignment = Alignment.Center ,
        modifier = Modifier
            .width(158.dp)
            .height(191.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                //starts the image picking flow
                onSelect()
            }
    ) {

        if (selectedImage.isNotBlank()){
            //will replace with coil
            Image(
                painterResource(id = R.drawable.image_placeholder_24),
                contentDescription = "taken book image" ,
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp) ,
                contentScale = ContentScale.Fit
            )
        }

        if (showPlaceHolder) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(id = R.drawable.add_a_photo_24),
                    contentDescription = "Add image placeholder" ,
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp) ,
                    tint = MaterialTheme.colorScheme.secondary

                )
                Text(
                    text = "Add image" ,
                    style = BookAppTypography.labelLarge ,
                    modifier = Modifier.padding(16.dp) ,
                    textAlign = TextAlign.Center ,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        if(showProgressBar){
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(name = "TextFieldComponent")
fun TextFieldComponent(
     text : String = "" ,
     label : String = "Some label" ,
     onTextChanged : (String) -> Unit = {}
){
    Column(
        verticalArrangement = Arrangement.Top ,
        horizontalAlignment = Alignment.Start ,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label ,
            style = BookAppTypography.labelMedium ,
            modifier = Modifier.fillMaxWidth()
           //also adjust error color and text if error is available
        )

        OutlinedTextField(
            value = text,
            onValueChange = {
                 onTextChanged(it)
            } ,
            modifier = Modifier
                .padding(top = 8.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(0.dp),
                    //if error is available , change the color of border to error color
                    color = MaterialTheme.colorScheme.secondary
                )
                .fillMaxWidth()
            ,

            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background ,
                cursorColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onPrimary ,
                errorLabelColor = MaterialTheme.colorScheme.error ,
                errorCursorColor = MaterialTheme.colorScheme.error
            ) ,
            textStyle = BookAppTypography.bodyMedium ,
            shape = RoundedCornerShape(0.dp) ,
            //adjust error depending if error is available
            isError = true ,
        )

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview("DropDownComponent")
fun DropDownComponent(
    modifier: Modifier = Modifier ,
    label : String = "some label" ,
    dropDownItems : List<String> = listOf() ,
    onListItemSelected : (String) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top ,
        horizontalAlignment = Alignment.Start
    ) {

        var expanded by remember { mutableStateOf(false) }
        var textFieldSize by remember { mutableStateOf(Size.Zero)}
        var selectedText by remember { mutableStateOf("") }
        
        Text(
            text = label ,
            style = BookAppTypography.labelMedium
        )

        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .width(180.dp)
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                }
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(0.dp),
                    color = MaterialTheme.colorScheme.secondary
                ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background ,
                textColor = MaterialTheme.colorScheme.onPrimary
            ) ,
            shape = RoundedCornerShape(0.dp)
        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            dropDownItems.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                    onListItemSelected(item)
                    expanded = false
                   } , 
                    text = {
                        Text(text = item , style = BookAppTypography.bodySmall)
                    }
                )
            }
        }

    }
}


