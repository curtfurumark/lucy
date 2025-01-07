package se.curtrune.lucy.activities.kotlin.monthcalendar

import se.curtrune.lucy.classes.calender.CalenderDate
import java.time.LocalDate
import java.time.YearMonth

data class MonthCalendarState(
    var yearMonth:YearMonth = YearMonth.now(),
    var calendarDates: List<CalenderDate> = mutableListOf<CalenderDate>(),
    var showAddItemDialog: Boolean = false
)