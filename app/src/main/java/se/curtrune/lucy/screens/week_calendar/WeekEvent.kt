package se.curtrune.lucy.screens.week_calendar

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week

sealed interface WeekEvent{
    data class AddItem(val item: Item): WeekEvent
    //data class Week(val week: Week?): WeekEvent
    data class CalendarDateClick(val calendarDate: CalenderDate): WeekEvent
    data object ShowAddItemDialog: WeekEvent
    data class OnAllWeekClick(val week: Week): WeekEvent
    data class OnPage(val page: Int): WeekEvent

}