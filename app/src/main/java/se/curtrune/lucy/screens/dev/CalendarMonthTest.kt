package se.curtrune.lucy.screens.dev

import android.content.Context
import se.curtrune.lucy.classes.calender.CalenderMonth
import se.curtrune.lucy.persist.CalenderWorker
import java.time.YearMonth

class CalendarMonthTest {

    companion object{
        fun getCalendarMonth(yearMonth: YearMonth, context: Context): CalenderMonth {
            println("getCalendarMonth(${yearMonth.toString()})")
            val calendarMonth = CalenderWorker.getCalenderMonth(yearMonth, context)
            println("calendarMonth ${calendarMonth.firstDateOfMonth.toString()}")
            return calendarMonth
        }
    }
}