package se.curtrune.lucy.screens.appointments.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.curtrune.lucy.screens.appointments.AppointmentEvent
import se.curtrune.lucy.screens.appointments.AppointmentsState
import se.curtrune.lucy.screens.timeline.composables.SortBar
import se.curtrune.lucy.screens.timeline.composables.SortEvent

@Composable
fun AppointmentsList(
    modifier: Modifier = Modifier,
    state: AppointmentsState,
    onEvent: (AppointmentEvent)->Unit,
    sortEvent: (SortEvent)->Unit
){
    LazyColumn(modifier = modifier.fillMaxWidth().fillMaxWidth()) {
        item {
            SortBar(onEvent = {
                sortEvent(it)
            })
        }
        items(state.items){ item->
            AppointmentCard(appointment = item, onEvent = { event->
                onEvent(event)
            })
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}