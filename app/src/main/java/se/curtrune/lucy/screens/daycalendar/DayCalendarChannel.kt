package se.curtrune.lucy.screens.daycalendar

import androidx.navigation3.runtime.NavKey
import se.curtrune.lucy.classes.item.Item

sealed interface DayCalendarChannel{
    data class ShowMessage(val message: String) : DayCalendarChannel
    data object ConfirmDeleteDialog: DayCalendarChannel
    data object ShowAddItemBottomSheet : DayCalendarChannel
    //data class EditItem(val item: Item) : DayCalendarChannel
    data class Navigate(val navKey: NavKey) : DayCalendarChannel
}