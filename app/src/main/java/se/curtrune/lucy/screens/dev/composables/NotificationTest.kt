package se.curtrune.lucy.screens.dev.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.features.notifications.Notification
import se.curtrune.lucy.screens.dev.DevEvent
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun NotificationTest(onEvent: (DevEvent) -> Unit = {}){
    val item = Item("test")
    val notification = Notification()
    notification.setTime(LocalTime.now().plusMinutes(5))
    notification.setDate(LocalDate.now())
    item.notification = notification
    Card(modifier = Modifier.fillMaxWidth()){
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text = "test notification")
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Text(text = "time: ${notification.getTime()}")
            Text(text = "date: ${notification.getDate()}")
        }
        Row(modifier = Modifier.fillMaxWidth()){
            Button(onClick = {
                onEvent(DevEvent.InsertItem(item))
            }){
                Text("notify me")
            }
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewTestNotification(){
    LucyTheme {
        NotificationTest()

    }
}