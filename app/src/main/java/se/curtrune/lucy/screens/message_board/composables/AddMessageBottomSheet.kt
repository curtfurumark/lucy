package se.curtrune.lucy.screens.message_board.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.message_board.Message
import java.time.LocalDateTime
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMessageBottomSheet(onDismiss: ()->Unit, onSave: (Message)->Unit){
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val message by remember{
        mutableStateOf(Message())
    }
    var subject by remember {
        mutableStateOf("")
    }
    val category by remember {
        mutableStateOf("message")
    }
    var content by remember {
        mutableStateOf("")
    }
    var user by remember {
        mutableStateOf("")
    }
    var isTodo by remember {
        mutableStateOf(false)
    }
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState =  sheetState)
    {
        //val windowInsets = LocalWindowInsets.
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.add_a_message_to_the_message_board),
                fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = subject,
                onValueChange = {
                    subject = it
                    message.subject = it},
                label = {Text(text = stringResource(R.string.subject))})
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = content,
                onValueChange = {
                    content = it
                    message.content = it},
                label = {Text(text = stringResource(R.string.content))})
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = user,
                onValueChange = {
                    user = it
                    message.user = it },
                label = {Text(text = stringResource(R.string.user))})
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismiss()
                        }
                    }
                }) {
                    Text(text = stringResource(R.string.dismiss))
                }
                Button(onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            message.category = category
                            message.created = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                            onSave(message)
                        }
                    }
                }) {
                    Text(text = "save")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun PreviewBottomSheet(){
    LucyTheme {
        AddMessageBottomSheet(onDismiss = {}, onSave = {})
    }
}