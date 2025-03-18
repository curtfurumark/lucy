package se.curtrune.lucy.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.Notification
import se.curtrune.lucy.util.DateTImeConverter
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDialog(onDismiss: ()->Unit, onConfirm: (Notification)->Unit){
    var notification by remember {
        mutableStateOf(Notification())
    }
    var showTimeDialog by remember {
        mutableStateOf(false)
    }
    var notificationSelected by remember {
        mutableStateOf(true)
    }
    var time by remember {
        mutableStateOf(LocalTime.of(0, 0))
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.notification),
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = notificationSelected, onClick = {
                        notificationSelected = !notificationSelected
                        println("on notification click")
                        notification.type = Notification.Type.NOTIFICATION
                    })
                    Text(text = stringResource(R.string.notification))
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = !notificationSelected, onClick = {
                        notificationSelected = !notificationSelected
                        println("on alarm click")
                        notification.type = Notification.Type.ALARM
                    })
                    Text(text = stringResource(R.string.alarm))
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            showTimeDialog = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.time))
                    Text(text = DateTImeConverter.format(time))
                }
                Row(modifier = Modifier.fillMaxWidth()
                    .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        onDismiss()
                    }) {
                        Text(text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = {
                        onConfirm(notification)
                    }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
        if(showTimeDialog){
            TimePickerDialog(onDismiss={
                showTimeDialog = false
            },
                onConfirm = { state->
                    time = LocalTime.of(state.hour, state.minute)
                    notification.time = time
                    showTimeDialog = false
                })
        }
    }
}
@Composable
@Preview
fun PreviewNotificationDialog(){
    LucyTheme {
        NotificationDialog(onDismiss = {}, onConfirm = {})
    }
}