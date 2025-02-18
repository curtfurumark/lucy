package se.curtrune.lucy.screens.daycalendar

sealed interface DayChannel{
    data object ConfirmDeleteDialog: DayChannel
}