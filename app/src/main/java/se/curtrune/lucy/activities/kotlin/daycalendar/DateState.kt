package se.curtrune.lucy.activities.kotlin.daycalendar

import se.curtrune.lucy.classes.Item
import java.time.LocalDate

data class DayCalendarState(
    var items: List<Item> = emptyList(),
    val date: LocalDate = LocalDate.now()
)