package se.curtrune.lucy.modules

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.screens.main.TopAppBarState
import se.curtrune.lucy.util.DateTImeConverter
import se.curtrune.lucy.util.cecilia
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

object TopAppbarModule{
    private val _topAppBarState: MutableStateFlow<TopAppBarState> = MutableStateFlow(TopAppBarState())
    val topAppBarState = _topAppBarState.asStateFlow()
    fun setTitle(title: String){
        _topAppBarState.update { it.copy(
            title = title
        ) }
    }
    fun setFilter(filter: String, searchEverywhere: Boolean){
        _topAppBarState.update { it.copy(
            filter = filter,
            searchEverywhere = searchEverywhere
        ) }
    }
    fun setTitle(yearMonth: YearMonth){
        _topAppBarState.update { it.copy(
            title =  DateTImeConverter.format(yearMonth).cecilia()
            )
        }
    }

    /**
     * Sets the title of the top app bar, like "May v20"
     * used by daycalendar
     */
    fun setTitle(date: LocalDate){
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).cecilia()
        val week = date.get(java.time.temporal.WeekFields.ISO.weekOfYear())
        val title = String.format(Locale.getDefault(), "%s v%d", month, week)
        _topAppBarState.update { it.copy(
            title = title
        ) }
    }
    fun setTitle(week: Week){
        var monthString = week.firstDateOfWeek.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).cecilia()
        if( week.firstDateOfWeek.month != week.lastDateOfWeek.month){
            monthString = monthString.plus("/${week.lastDateOfWeek.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).cecilia()}")
        }
        val title = String.format(Locale.getDefault(), "%s v%d", monthString, week.weekNumber)

        _topAppBarState.update { it.copy(
            title = title
        ) }
    }
    fun setShowMental(show: Boolean){
        _topAppBarState.update {
            it.copy(
                showMental = show
            )
        }
    }
}