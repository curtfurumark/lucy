package se.curtrune.lucy.screens.my_day

import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate

data class MyDayState(
    val items: List<Item> = emptyList(),
    val currentField: Field = Field.ENERGY,
    val allDay: Boolean = false,
    val date: LocalDate = LocalDate.now(),
    val showDatePicker: Boolean = false
)