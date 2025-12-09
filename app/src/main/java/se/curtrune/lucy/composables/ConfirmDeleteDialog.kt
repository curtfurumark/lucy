package se.curtrune.lucy.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import se.curtrune.lucy.R
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.daycalendar.DayCalendarEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDeleteDialog(item: Item, onDismiss: ()->Unit, onEvent: (DayCalendarEvent)->Unit){
    Surface(modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.height(200.dp)
            .background(color = MaterialTheme.colorScheme.background),
        properties = DialogProperties(usePlatformDefaultWidth = true)) {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween){
            Text(text = "delete ${item.heading}?", fontSize = 20.sp)
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    onDismiss()
                }) {
                    Text(stringResource(R.string.dismiss
                    ))
                }
                Button(onClick = {
                    onEvent(DayCalendarEvent.DeleteItem(item))
                }) {
                    Text(stringResource(R.string.delete))
                }
            }
        }
        }
    }
}
@Composable
@Preview
fun PreviewConfirmDeleteDialog(){
    ConfirmDeleteDialog(
        item = Item("coffee break"),
        onDismiss = {},
        onEvent = {}
        )
}