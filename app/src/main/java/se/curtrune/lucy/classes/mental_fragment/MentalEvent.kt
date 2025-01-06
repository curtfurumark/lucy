package se.curtrune.lucy.classes.mental_fragment

import se.curtrune.lucy.activities.kotlin.composables.Field
import se.curtrune.lucy.classes.Item
import java.time.LocalDate

sealed interface MentalEvent {
    data class SetEditField(val field: Field): MentalEvent
    data class UpdateItem(val item: Item): MentalEvent
    data class SetDate(val date: LocalDate): MentalEvent
}