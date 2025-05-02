package se.curtrune.lucy.screens.daycalendar

sealed interface DayChannel{
    data class ShowMessage(val message: String) : DayChannel
    data object ConfirmDeleteDialog: DayChannel
    data object showAddItemBottomSheet : DayChannel
}