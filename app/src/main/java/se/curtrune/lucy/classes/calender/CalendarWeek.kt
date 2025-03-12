package se.curtrune.lucy.classes.calender

import se.curtrune.lucy.classes.Item

data class CalendarWeek(
    val week: Week = Week(),
    val calendarDates: List<CalenderDate> = emptyList(),
    val allWeekItems: List<Item> = emptyList()
)