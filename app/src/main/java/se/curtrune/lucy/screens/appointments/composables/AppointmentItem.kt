package se.curtrune.lucy.screens.appointments.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.activities.kotlin.ui.theme.LucyTheme
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.screens.appointments.AppointmentEvent
import se.curtrune.lucy.util.DateTImeConverter


@Composable
fun AppointmentItem(appointment: Item, onEvent: (AppointmentEvent)->Unit){
    var isDone by remember {
        mutableStateOf(appointment.isDone)
    }
    Card(modifier = Modifier.fillMaxWidth()
        .clickable {
            onEvent(AppointmentEvent.Edit(appointment))
        }) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = isDone, onCheckedChange = { checked ->
                    appointment.setIsDone(checked)
                    onEvent(AppointmentEvent.Update(appointment))
                    isDone = !isDone
                })
                Column(Modifier.weight(1f)) {
                    Row() {
                        Text(
                            text = appointment.heading,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                            maxLines = 1
                        )
                    }
                    Row(){
                        Text(
                            text = "${DateTImeConverter.format(appointment.targetTime)}, ${
                                DateTImeConverter.format(
                                    appointment.targetDate
                                )
                            }",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

            }


                if (appointment.comment.isNotBlank()) {
                    Text(
                        text = appointment.comment,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
    }
}
@Composable
fun TestAppointment(){
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,){
            Text(text = "row")
            Column(){
                Row() {
                    Text(text = "heading")
                }
                Row() {
                    Text(text = "description")
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewAppointment(){
    LucyTheme {
        //TestAppointment()
        AppointmentItem(appointment = Item("hello"), onEvent = {})
    }
}