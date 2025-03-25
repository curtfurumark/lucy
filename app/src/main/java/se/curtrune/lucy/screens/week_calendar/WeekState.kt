package se.curtrune.lucy.screens.week_calendar

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.calender.CalendarWeek
import se.curtrune.lucy.classes.calender.Week

data class WeekState(
    //val calendarDates: List<CalenderDate> = emptyList(),
    val calendarWeek: CalendarWeek = CalendarWeek(),
    val showContextMenu: Boolean = false,
    val currentWeek: Week = Week(),
    val currentParent: Item? = null,
    val numPages: Int = 10,
    val initialPage: Int = 5
)