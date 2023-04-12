package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/*
* Common standard toolbar used across multiple screens except the main start screens*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview()
fun StandardToolBar(
    navigate : () -> Unit = {},
    showBackArrow : Boolean = true,
    navActions : @Composable RowScope.() -> Unit = {},
    title : @Composable () -> Unit = {}
){

    TopAppBar(
        title = title,
        navigationIcon = {
            if (showBackArrow) {
                IconButton(
                    onClick = {
                        navigate()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = navActions,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )

}