package se.curtrune.lucy.screens.mental

import se.curtrune.lucy.composables.Field
import se.curtrune.lucy.classes.Item
import java.time.LocalDate

data class MentalState(
    val items: List<Item> = emptyList(),
    val currentField: Field = Field.ENERGY,
    val allDay: Boolean = false,
    val date: LocalDate = LocalDate.now(),
    val showDatePicker: Boolean = false
)