package se.curtrune.lucy.activities.kotlin.daycalendar

import se.curtrune.lucy.classes.Item
import java.time.LocalDate

sealed interface DateEvent{
    data class AddItem(val item: Item): DateEvent
    data class DeleteItem(val item: Item): DateEvent
    data class EditItem(val item: Item): DateEvent
    data class EditTime(val item: Item): DateEvent
    data class CurrentDate(val date: LocalDate): DateEvent
    data class ShowActionsMenu(val item: Item): DateEvent
    data class UpdateItem(val item: Item): DateEvent
}