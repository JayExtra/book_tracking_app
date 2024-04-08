package com.dev.james.my_library.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import com.dev.james.booktracker.compose_ui.R
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.ui.components.CoilImageComponent
import com.dev.james.booktracker.compose_ui.ui.components.StandardToolBar

@Composable
fun ReadingListScreen(
    readingListId : String = ""
){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessReadingListScreen(){
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize() ,
        topBar = {
            StandardToolBar(
                title = {
                    Text(text = "Dummy Title")
                },
                showBackArrow = true,
                navActions = {
                    IconButton(
                        modifier = Modifier.clip(CircleShape) ,
                        onClick = { /*TODO 1 : Delete reading list action*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "" ,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        } ,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState),
                contentAlignment = Alignment.TopCenter
            ) {

            }
        }
    )
}

@Composable
fun ImageSection(
    modifier : Modifier = Modifier ,
    imagesList : List<String> = emptyList() ,
    onAddImage : () -> Unit = {}
){
    Box(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(5.dp)) ,
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp)
        ) {
            LazyVerticalGrid(
                modifier = Modifier.size(200.dp) ,
                columns = GridCells.Fixed(2)
            ){
                if(imagesList.isEmpty()){
                    item {
                        Icon(
                            modifier = Modifier.size(100.dp),
                            painter = painterResource(id = R.drawable.ic_image),
                            contentDescription ="" ,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }else if(imagesList.size == 1) {
                    for (i in 0..3){
                        item {
                            CoilImageComponent(
                                image = imagesList[0] ,
                                modifier = Modifier.width(90.dp).height(120.dp)
                            )
                        }
                    }
                }else if(imagesList.size == 2) {
                    for (i in 0..3){
                        item {
                            CoilImageComponent(
                                image = imagesList[i] ,
                                modifier = Modifier.width(90.dp).height(120.dp)
                            )
                        }
                    }

                }

            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp)
                .background(color = Color.Transparent) ,
            contentAlignment = Alignment.BottomEnd
        ){
            IconButton(
                modifier = Modifier.clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = { onAddImage() }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "" ,
                    tint = MaterialTheme.colorScheme.background
                )
            }

        }
    }

}