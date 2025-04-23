package se.curtrune.lucy.screens.message_board.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.message_board.Message
import java.time.LocalDateTime
import java.time.ZoneOffset


enum class Mode{
    EDIT, CREATE
}

data class DefaultMessage(
    val mode: Mode = Mode.CREATE,
    val message: Message = Message()
)
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMessageBottomSheet(defaultMessage: DefaultMessage, onDismiss: ()->Unit, onSave: (Message, Mode)->Unit){
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    var message by remember{
        mutableStateOf(defaultMessage.message)
    }
    val mode by remember {
        mutableStateOf(defaultMessage.mode)
    }

    var subject by remember {
        mutableStateOf(defaultMessage.message.subject)
    }
    val category by remember {
        mutableStateOf(defaultMessage.message.category)
    }
    var content by remember {
        mutableStateOf(defaultMessage.message.content)
    }
    var user by remember {
        mutableStateOf(defaultMessage.message.user)
    }
    val focusRequester =  remember {
        FocusRequester()
    }
    val headingID by remember {
        mutableIntStateOf(if(mode == Mode.EDIT) R.string.edit_message else R.string.add_a_message_to_the_message_board)
    }

    val focusManager = LocalFocusManager.current
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState =  sheetState)
    {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusRequester),
                textAlign = TextAlign.Center,
                text = stringResource(headingID),
                fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                value = subject,
                onValueChange = {
                    subject = it
                    message.subject = it},
                label = {Text(text = stringResource(R.string.subject))},
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        val stat = focusManager.moveFocus(FocusDirection.Down)
                        println("move focus down: $stat")
                    }
                )
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .focusable(true),
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
            if( mode ==Mode.EDIT){
                Row(){
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    var state by remember {
                        mutableStateOf(Message.State.entries[message.state].name)
                    }
                    TextButton(onClick = {
                        expanded = !expanded
                    }) {
                        Text(
                            text = Message.State.entries[message.state].name
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = {
                        expanded = false
                    }) {
                        Message.State.entries.forEach{
                            println("menu text: ${it.name}")
                            DropdownMenuItem(text = {
                                Text(it.name)}, onClick = {
                                    state = it.name
                                    message.state = it.ordinal
                                    expanded = false
                            }, colors = MenuItemColors(
                                    textColor = MaterialTheme.colorScheme.onSurface,
                                    leadingIconColor = MaterialTheme.colorScheme.background,
                                    trailingIconColor = MaterialTheme.colorScheme.background,
                                    disabledTextColor = MaterialTheme.colorScheme.background,
                                    disabledLeadingIconColor = MaterialTheme.colorScheme.background,
                                    disabledTrailingIconColor = MaterialTheme.colorScheme.background,
                            )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
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
                            onSave(message, mode)
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
@PreviewLightDark
@Preview(showBackground = true)
fun PreviewBottomSheet(){
    LucyTheme {
        val message = Message()
        message.subject = "test"
        message.content = "test"
        message.user = "curt rune"
        AddMessageBottomSheet(defaultMessage = DefaultMessage(Mode.CREATE, message), onDismiss = {}, onSave = { _, _->
            println("onSave")
        })
    }
}