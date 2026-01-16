package se.curtrune.lucy.screens.monthcalendar

import se.curtrune.lucy.classes.calender.CalendarDate
import se.curtrune.lucy.classes.calender.CalenderMonth
import java.time.YearMonth

data class MonthCalendarState(
    var yearMonth:YearMonth = YearMonth.now(),
    var calendarMonth: CalenderMonth? = null,
    var currentCalendarDate: CalendarDate? = null,
    var pageCount: Int = 24,
    var initialPage: Int = 50
)