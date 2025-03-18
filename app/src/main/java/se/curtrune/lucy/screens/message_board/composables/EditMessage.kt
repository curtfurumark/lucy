package se.curtrune.lucy.screens.message_board.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import se.curtrune.lucy.R
import se.curtrune.lucy.screens.message_board.Message
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun EditMessage(
    onDismiss: ()->Unit,
    message: Message,
    onUpdate: (Message)->Unit){
    var subject by remember {
        mutableStateOf(message.subject)
    }
    var content by remember {
        mutableStateOf(message.content)
    }
    var category by remember {
        mutableStateOf(message.category)
    }
    var user by remember {
        mutableStateOf(message.user)
    }
    Column {
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
            label = { Text(text = stringResource(R.string.subject)) })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = content,
            onValueChange = {
                content = it
                message.content = it},
            label = { Text(text = stringResource(R.string.content)) })
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = user,
            onValueChange = {
                user = it
                message.user = it },
            label = { Text(text = stringResource(R.string.user)) })
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(R.string.dismiss))
            }
            Button(onClick = {
                onUpdate(message)
            }) {
                Text(text = "save")
            }
        }
    }
}
