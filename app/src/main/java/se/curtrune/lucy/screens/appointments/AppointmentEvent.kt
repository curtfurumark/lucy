package se.curtrune.lucy.screens.appointments

import se.curtrune.lucy.classes.item.Item

sealed interface AppointmentEvent {
    data class InsertAppointment(val item: Item): AppointmentEvent
    data class DeleteAppointment(val item: Item): AppointmentEvent
    data class Edit(val item: Item): AppointmentEvent
    data class Filter(val filter: String, val everywhere: Boolean): AppointmentEvent
    data object ShowAddAppointmentDialog: AppointmentEvent
    data class Update(val item: Item): AppointmentEvent
}