package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.Item
import java.time.LocalDate

data class DayCalendarState(
    //var items: List<Item> = emptyList(),
    //var items: MutableList<Item> = mutableListOf()
    var currentItem: Item? = null,
    var editItem: Boolean = false,
    var items: List<Item> = mutableListOf(),
    val date: LocalDate = LocalDate.now()
)