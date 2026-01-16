package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.dialogs.PostponeDetails
import java.time.LocalDate

sealed interface DayCalendarEvent{
    data class AddItem(val item: Item): DayCalendarEvent
    data class DeleteItem(val item: Item): DayCalendarEvent
    data class Duplicate(val item: Item): DayCalendarEvent
    data class EditItem(val item: Item): DayCalendarEvent
    data class EditTime(val item: Item): DayCalendarEvent
    data class CurrentDate(val date: LocalDate): DayCalendarEvent
    data object RestoreDeletedItem: DayCalendarEvent
    data class ShowActionsMenu(val item: Item): DayCalendarEvent
    data class Postpone(val postponeInfo: PostponeDetails): DayCalendarEvent
    data class RequestDelete(val item: Item): DayCalendarEvent
    data class ShowPostponeDialog(val item: Item): DayCalendarEvent
    data object HidePostponeDialog: DayCalendarEvent
    data class Search(val filter: String, val everywhere: Boolean): DayCalendarEvent
    data object ShowAddItemBottomSheet : DayCalendarEvent
    data class ShowChildren(val item: Item): DayCalendarEvent
    data class ShowStats(val item: Item):DayCalendarEvent
    data class StartTimer(val item: Item):DayCalendarEvent
    data class ShowTodoItems(val show: Boolean =  false):DayCalendarEvent
    data class TabSelected(val index: Int, val item: Item?): DayCalendarEvent
    data class UpdateItem(val item: Item): DayCalendarEvent
    data class Week(val page: Int):DayCalendarEvent
    data class AddList(val item: Item): DayCalendarEvent
}