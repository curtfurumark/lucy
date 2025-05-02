package se.curtrune.lucy.classes.calender

import se.curtrune.lucy.classes.item.Item

data class CalendarWeek(
    val week: Week = Week(),
    val calendarDates: List<CalenderDate> = emptyList(),
    var allWeekItems: List<Item> = emptyList()
)