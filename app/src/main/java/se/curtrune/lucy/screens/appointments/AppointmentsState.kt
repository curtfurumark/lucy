package se.curtrune.lucy.screens.appointments

import se.curtrune.lucy.classes.Item

data class AppointmentsState(
    val items: List<Item> = emptyList(),
    val editAppointment: Boolean = false,
    val currentAppointment: Item? = null
)
