package se.curtrune.lucy.modules

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.screens.main.TopAppBarState
import se.curtrune.lucy.util.DateTImeConverter
import se.curtrune.lucy.util.cecilia
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
    fun setTitle(week: Week){
        val title = String.format(Locale.getDefault(), "%s v%d", week.month.getDisplayName(TextStyle.FULL, Locale.getDefault()).cecilia(), week.weekNumber)
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