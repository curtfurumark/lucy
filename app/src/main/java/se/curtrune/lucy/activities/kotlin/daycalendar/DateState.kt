package se.curtrune.lucy.activities.kotlin.daycalendar

import se.curtrune.lucy.classes.Item
import java.time.LocalDate

data class DayCalendarState(
    //var items: List<Item> = emptyList(),
    var items: MutableList<Item> = mutableListOf(),
    val date: LocalDate = LocalDate.now()
)