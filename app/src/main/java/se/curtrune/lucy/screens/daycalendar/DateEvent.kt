package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.composables.PostponeDetails
import java.time.LocalDate

sealed interface DateEvent{
    data class AddItem(val item: Item): DateEvent
    data class DeleteItem(val item: Item): DateEvent
    data class EditItem(val item: Item): DateEvent
    data class EditTime(val item: Item): DateEvent
    data class CurrentDate(val date: LocalDate): DateEvent
    data object RestoreDeletedItem: DateEvent
    data class ShowActionsMenu(val item: Item): DateEvent
    data class UpdateItem(val item: Item): DateEvent
    data class Postpone(val postponeInfo: PostponeDetails): DateEvent
    data class ShowChildren(val item: Item): DateEvent
    data class ShowPostponeDialog(val item: Item): DateEvent
    data object HidePostponeDialog: DateEvent
    data class Search(val filter: String, val everywhere: Boolean): DateEvent
    data class ShowStats(val item: Item):DateEvent
    data class StartTimer(val item: Item):DateEvent
    data class TabSelected(val index: Int): DateEvent
}