package se.curtrune.lucy.activities.kotlin.monthcalendar

import se.curtrune.lucy.classes.calender.CalenderDate
import java.time.YearMonth

class MonthCalendarState {
    var yearMonth = YearMonth.now()
    var calendarDates = mutableListOf<CalenderDate>()
    var showAddItemDialog = false
}