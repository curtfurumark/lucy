package se.curtrune.lucy.screens.appointments

import se.curtrune.lucy.classes.item.Item

sealed interface AppointmentChannel{
    data class EditItem(val item: Item): AppointmentChannel
    data object ShowAddItemDialog: AppointmentChannel
    data class NavigateDetails(val appointment:Item): AppointmentChannel
    data class ShowMessage(val message: String): AppointmentChannel
}