package se.curtrune.lucy.screens.monthcalendar

sealed interface MonthChannel{
    data class ShowMessage(val message: String): MonthChannel
    data object ShowAddItemDialog: MonthChannel
}
