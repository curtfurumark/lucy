package se.curtrune.lucy.screens.monthcalendar

import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import java.time.YearMonth

sealed interface MonthCalendarEvent {
    data class InsertItem(val item: Item): MonthCalendarEvent
    data class CalendarDateClick(val calendarDate: CalenderDate): MonthCalendarEvent
    data class MonthYear( val  yearMonth: YearMonth): MonthCalendarEvent
    data class Pager(val page: Int): MonthCalendarEvent
    data object ShowYearMonthDialog: MonthCalendarEvent
}