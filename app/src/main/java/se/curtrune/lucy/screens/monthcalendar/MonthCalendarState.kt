package se.curtrune.lucy.screens.monthcalendar

import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.CalenderMonth
import java.time.LocalDate
import java.time.YearMonth

data class MonthCalendarState(
    var yearMonth:YearMonth = YearMonth.now(),
    var calendarMonth: CalenderMonth? = null,
    var currentCalendarDate: CalenderDate? = null,
    var pageCount: Int = 24,
    var initialPage: Int = 12
)