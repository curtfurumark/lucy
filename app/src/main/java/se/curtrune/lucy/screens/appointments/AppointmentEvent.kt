package se.curtrune.lucy.screens.appointments

import se.curtrune.lucy.classes.Item

sealed interface AppointmentEvent {
    data class AddAppointment(val item: Item): AppointmentEvent
    data class DeleteAppointment(val item: Item): AppointmentEvent
    data class Edit(val item: Item): AppointmentEvent
    data class Update(val item: Item): AppointmentEvent
}