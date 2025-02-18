package se.curtrune.lucy.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.daycalendar.DayEvent


@Composable
fun ConfirmDeleteDialog(item: Item, onDismiss: ()->Unit, onEvent: (DayEvent)->Unit){
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = true),
        onDismissRequest = {

    }) {
        Surface(
            modifier = Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "delete ${item.heading}?")
                Row() {
                    Button(onClick = {
                        onDismiss()
                    }) {
                        Text("dismiss")
                    }
                    Button(onClick = {
                        onEvent(DayEvent.DeleteItem(item))
                    }) {
                        Text("delete")
                    }
                }
            }
        }
    }
}