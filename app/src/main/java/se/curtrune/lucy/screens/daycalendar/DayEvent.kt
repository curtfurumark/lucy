package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.PostponeDetails
import java.time.LocalDate

sealed interface DayEvent{
    data class AddItem(val item: Item): DayEvent
    data class DeleteItem(val item: Item): DayEvent
    data class EditItem(val item: Item): DayEvent
    data class EditTime(val item: Item): DayEvent
    data class CurrentDate(val date: LocalDate): DayEvent
    //data class InsertWithID(val item: Item): MyDateEvent.Date
    data object RestoreDeletedItem: DayEvent
    data class ShowActionsMenu(val item: Item): DayEvent
    data class Postpone(val postponeInfo: PostponeDetails): DayEvent
    data class RequestDelete(val item: Item): DayEvent
    data class ShowChildren(val item: Item): DayEvent
    data class ShowPostponeDialog(val item: Item): DayEvent
    data object HidePostponeDialog: DayEvent
    data class Search(val filter: String, val everywhere: Boolean): DayEvent
    data class ShowStats(val item: Item):DayEvent
    data class StartTimer(val item: Item):DayEvent
    data class TabSelected(val index: Int, val item: Item?): DayEvent
    data class UpdateItem(val item: Item): DayEvent
    data class Week(val page: Int):DayEvent
}