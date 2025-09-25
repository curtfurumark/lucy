package se.curtrune.lucy.screens.appointments

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.DefaultItemSettings

data class AppointmentsState(
    val items: List<Item> = emptyList(),
    var editAppointment: Boolean = false,
    val currentAppointment: Item? = null,
    var showAddAppointmentDialog: Boolean = false,
    val defaultItemSettings: DefaultItemSettings = DefaultItemSettings(
        item = Item().also {
            it.setIsAppointment(true)
            it.setIsCalenderItem(true)
            //it.setIsCalendarItem(true)
        }
    )
)
