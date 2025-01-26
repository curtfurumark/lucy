package se.curtrune.lucy.screens.mental

import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.classes.Item
import java.time.LocalDate

sealed interface MentalEvent {
    data class SetEditField(val field: Field): MentalEvent
    data class UpdateItem(val item: Item): MentalEvent
    data class AllDay(val allDay: Boolean): MentalEvent
    data class SetDate(val date: LocalDate): MentalEvent
    data class ShowDatePicker(val show: Boolean): MentalEvent
}