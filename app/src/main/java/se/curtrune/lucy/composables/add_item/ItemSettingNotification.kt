package se.curtrune.lucy.composables.add_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.features.notifications.Notification
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.dialogs.NotificationDialog
import java.time.LocalTime

@Composable
fun ItemSettingNotification(item: Item, onNotication: (Notification) -> Unit){
    var showNotificationDialog by remember {
        mutableStateOf(false)
    }
    val hasNotification = item.notification != null
    Card(modifier = Modifier.fillMaxWidth()
        .padding(8.dp)
        .clickable {
            showNotificationDialog = true
        }) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if( hasNotification){
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "notification")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.notification.toString())
            }else{
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "notification")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.notification))
            }
        }
    }
    if(showNotificationDialog){
        NotificationDialog(onDismiss = {
            showNotificationDialog = false
        }, onConfirm = { notification->
            item.notification = notification
            onNotication(notification)
            showNotificationDialog = false
        })
    }
}

@Composable
@PreviewLightDark
fun PreviewItemSettingNotification(){
    LucyTheme {
        val item = Item("with notification")
        val notification = Notification()
        notification.setTime(LocalTime.now())
        item.notification = notification
        ItemSettingNotification(item = item, onNotication = {})

    }
}