package se.curtrune.lucy.screens.appointments.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.screens.appointments.AppointmentEvent
import se.curtrune.lucy.screens.util.Converter


@Composable
fun AppointmentItem(appointment: Item, onEvent: (AppointmentEvent)->Unit){
    Card(modifier = Modifier.fillMaxWidth()
        .clickable {
            onEvent(AppointmentEvent.Edit(appointment))
        }){
        Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = appointment.isDone, onCheckedChange = {
                appointment.setIsDone(it)
                onEvent(AppointmentEvent.Update(appointment))
            })
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = appointment.heading,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                maxLines =  1)
        }

        Text(
            text = "${Converter.format(appointment.targetTime)}, ${Converter.format(appointment.targetDate)}",
            modifier = Modifier.padding(start = 8.dp))
        if( appointment.comment.isNotBlank()) {
            Text(text = appointment.comment,
                modifier = Modifier.padding(start = 8.dp))
        }
    }
}