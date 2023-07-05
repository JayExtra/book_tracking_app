package com.dev.james.booktracker.home.presentation.components

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography
import com.dev.james.booktracker.core.common_models.Book
import com.dev.james.booktracker.home.R
import com.dev.james.booktracker.home.presentation.viewmodels.ReadGoalsScreenViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleBooksSearchBottomSheet(
    readGoalsScreenViewModel : ReadGoalsScreenViewModel = hiltViewModel()
){

    StateLessGoogleBooksSearchBottomSheet()

}

@Composable
@Preview("GoogleSearchBottomSheet" , showBackground = true)
fun StateLessGoogleBooksSearchBottomSheet(
    isFetching : Boolean = false ,
    onSearchTextChanged : (String) -> Unit = {}  ,
    onBookSelected : (Book) -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp) ,
        verticalArrangement = Arrangement.spacedBy(16.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldComponent(
            modifier = Modifier.fillMaxWidth() ,
            hint = "Search for any book" ,
            startingIcon = Icons.Default.Search ,
            trailingIcon = Icons.Default.Close,
            onClearTextField = {
                //update the textfield state to empty

            },
            onTextChanged = { query ->
                onSearchTextChanged(query)
            }
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            if(isFetching){
                CircularProgressIndicator(
                    strokeWidth = 3.dp ,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp) ,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentPadding = PaddingValues(8.dp),
                content = {

                }
            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun BookInformationCard(
    onBookSelected : () -> Unit = {} ,
){

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp) ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painterResource(id = R.drawable.image_placeholder_24),
            contentDescription = "book thumbnail" ,
            modifier = Modifier
                .height(150.dp)
                .width(120.dp)
                .clip(shape = RoundedCornerShape(10.dp)) ,
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp) ,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
             Text(
                 text = "Some book title" ,
                 style = BookAppTypography.headlineMedium
             )

            Text(
                text = "By: Some book authors" ,
                style = BookAppTypography.bodySmall
            )
        }
    }

}