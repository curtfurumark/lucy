package se.curtrune.lucy.screens.message_board.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Message
import se.curtrune.lucy.util.DateTImeFormatter

@Composable
fun MessageView(message: Message){
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(4.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = message.subject)
            Text(text = message.content)
            Text(text = message.user)
            Text(text = DateTImeFormatter.format(message.created))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMessage(){
    LucyTheme {
        val message = Message()
        message.subject ="updating message board"
        message.user = "curt rune"
        message.content = "hello i am working on it, hold your horses, whatever, and then some" +
                "its not working, or is it? who am i to tell"
        MessageView(message = message)
    }
}