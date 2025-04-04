package se.curtrune.lucy.screens.week_calendar

import androidx.fragment.app.Fragment
import se.curtrune.lucy.classes.calender.CalenderDate
import java.time.LocalDate

sealed interface WeekChannel{
    data class Navigate(var fragment: Fragment): WeekChannel
    data class ViewDay(val calendarDate: CalenderDate): WeekChannel
    data class ShowMessage(val message: String): WeekChannel
    data class ShowAddItemDialog(val date: LocalDate): WeekChannel
    data object NavigateMyDay : WeekChannel
    data object ShowAddAllWeekNote: WeekChannel
}