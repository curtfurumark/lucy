package se.curtrune.lucy.screens.my_day

import se.curtrune.lucy.classes.Item
import java.time.LocalDate

sealed interface MyDateEvent {
    data class SetEditField(val field: Field): MyDateEvent
    data class UpdateItem(val item: Item): MyDateEvent
    data class AllDay(val allDay: Boolean): MyDateEvent
    data class Field(val field: se.curtrune.lucy.composables.Field): MyDateEvent
    data class Date(val date: LocalDate): MyDateEvent
    data class ShowDatePicker(val show: Boolean): MyDateEvent
}