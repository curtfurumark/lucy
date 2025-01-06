package se.curtrune.lucy.classes.mental_fragment

import se.curtrune.lucy.activities.kotlin.composables.Field
import se.curtrune.lucy.classes.Item
import java.time.LocalDate

data class MentalState(
    val items: List<Item> = emptyList(),
    val currentField: Field = Field.ENERGY,
    val date: LocalDate = LocalDate.now()
)