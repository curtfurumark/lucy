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

@Composable
fun AppointmentsScreen(state: AppointmentsState, onEvent: (AppointmentEvent)->Unit){
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.items){ item->
            AppointmentItem(appointment = item, onEvent = {
                //println(" onEvent ${it.toString()}")
                onEvent(AppointmentEvent.Edit(item))
            })
            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}