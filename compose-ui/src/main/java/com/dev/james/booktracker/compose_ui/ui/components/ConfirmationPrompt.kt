package com.dev.james.booktracker.compose_ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dev.james.booktracker.compose_ui.R
import com.dev.james.booktracker.compose_ui.ui.theme.BookAppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationPrompt(
    modifier : Modifier = Modifier,
    title : String = "",
    body : String = "",
    showCheckBox : Boolean = false ,
    checkBoxMessage : String = "",
    onAccept : (Boolean) -> Unit = {},
    onCancel : () -> Unit = {},
    confirmationText : String = stringResource(R.string.confirm),
    cancelText : String = stringResource(R.string.cancel)
){
    var checkBoxState by remember {
        mutableStateOf(false )
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onCancel() }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth() ,
            tonalElevation = AlertDialogDefaults.TonalElevation ,
            color = MaterialTheme.colorScheme.background ,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp) ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title ,
                    style = BookAppTypography.headlineMedium ,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = body ,
                    style = BookAppTypography.bodyMedium ,
                    textAlign = TextAlign.Center
                )

                if(showCheckBox){
                    Row (
                        verticalAlignment = Alignment.CenterVertically ,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Checkbox(
                            checked = checkBoxState,
                            onCheckedChange = {
                                checkBoxState = !checkBoxState
                            }
                        )
                        Text(modifier = Modifier.weight(3f),text = checkBoxMessage , style = BookAppTypography.labelSmall)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth() ,
                    verticalAlignment = Alignment.CenterVertically ,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    OutlinedButton(
                        onClick = { onCancel() } ,
                    ) {
                        Text(text = cancelText)
                    }
                    ElevatedButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = { onAccept(
                            checkBoxState
                        ) }
                    ) {
                        Text(text = confirmationText)
                    }
                }

            }

        }
    }

}

/*
@PreviewLightDark
@Composable
fun ConfirmationDialogPreview(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.0f)
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp)
    ){
        ConfirmationPrompt(
            title = "Test title" ,
            body = "This is some test body innit! Have a good one" ,
            onAccept = {} ,
            onCancel = {}
        )
    }
}*/
