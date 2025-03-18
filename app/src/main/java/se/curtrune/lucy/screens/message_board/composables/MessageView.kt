package se.curtrune.lucy.screens.message_board.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.screens.message_board.Message
import se.curtrune.lucy.screens.message_board.MessageBoardEvent
import se.curtrune.lucy.util.DateTImeConverter

@Composable
fun MessageView(message: Message, onEvent: (MessageBoardEvent)->Unit){
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onEvent(MessageBoardEvent.OnMessageClick(message))
            }
            .padding(4.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = message.subject, fontSize = 20.sp)
                if (message.category == "todo") {
                    Text(text = Message.State.entries[message.state].name)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message.content)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message.user)
            Text(text = DateTImeConverter.epochSecondToFormattedDateTimeString(message.created))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMessage(){
    LucyTheme {
        val message = Message()
        message.subject ="updating message board"
        message.category = "todo"
        message.state = Message.State.WIP.ordinal
        message.user = "curt rune"
        message.content = "hello i am working on it, hold your horses, whatever, and then some" +
                "its not working, or is it? who am i to tell"
        MessageView(message = message, onEvent = {})
    }
}