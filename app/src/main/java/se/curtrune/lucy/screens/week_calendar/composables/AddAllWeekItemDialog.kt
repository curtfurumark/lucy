package se.curtrune.lucy.screens.week_calendar.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import se.curtrune.lucy.R
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate

@Composable
fun AddAllWeekItemDialog(onDismiss: () -> Unit, onConfirm: (Item) -> Unit) {
    val item by remember {
        mutableStateOf(Item())
    }
    item.itemDuration = ItemDuration(ItemDuration.Type.WEEK)
    item.targetDate = LocalDate.now()
    item.isCalenderItem = true
    var heading by remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.all_week_heading), fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = heading, onValueChange = {
                    item.heading = it
                    heading= it})
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(onClick = {
                        onDismiss()
                    }) {
                        Text(
                            text = stringResource(R.string.dismiss))
                    }
                    Button(onClick = {
                        onConfirm(item)
                    }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }

            }
        }
    }
}
@Composable
@Preview
fun PreviewAllWeekWhatever(){
    LucyTheme {
       AddAllWeekItemDialog(onDismiss = {}, onConfirm = {})
    }

}