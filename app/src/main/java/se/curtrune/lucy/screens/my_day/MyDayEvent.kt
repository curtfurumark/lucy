package se.curtrune.lucy.screens.my_day

import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate

sealed interface MyDayEvent {
    data class SetEditField(val field: Field): MyDayEvent
    data class UpdateItem(val item: Item): MyDayEvent
    data class AllDay(val allDay: Boolean): MyDayEvent
    data class Field(val field: se.curtrune.lucy.composables.Field): MyDayEvent
    data class Date(val date: LocalDate): MyDayEvent
    data class ShowDatePicker(val show: Boolean): MyDayEvent
}