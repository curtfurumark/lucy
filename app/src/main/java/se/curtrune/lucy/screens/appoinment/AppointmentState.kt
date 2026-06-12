package se.curtrune.lucy.screens.appoinment

import se.curtrune.lucy.classes.item.Item

data class AppointmentState(
    val appointment: Item,
    val children: List<Item> = emptyList()
)