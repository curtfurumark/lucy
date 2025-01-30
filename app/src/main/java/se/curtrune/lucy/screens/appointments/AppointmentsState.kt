package se.curtrune.lucy.screens.appointments

import se.curtrune.lucy.classes.Item

data class AppointmentsState(
    val items: List<Item> = emptyList(),
    var editAppointment: Boolean = false,
    val currentAppointment: Item? = null,
    var showAddAppointmentDialog: Boolean = false
)
