package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.item.Item

sealed interface DayChannel{
    data class ShowMessage(val message: String) : DayChannel
    data object ConfirmDeleteDialog: DayChannel
    data object showAddItemBottomSheet : DayChannel
    data class EditItem(val item: Item) : DayChannel
    data object ShowNavigationDrawer : DayChannel

}