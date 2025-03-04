package se.curtrune.lucy.screens.monthcalendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.persist.CalenderWorker
import se.curtrune.lucy.persist.ItemsWorker
import se.curtrune.lucy.screens.daycalendar.DayChannel
import java.time.YearMonth

class MonthViewModel: ViewModel() {
    private val repository = LucindaApplication.repository
    private var currentYearMonth = YearMonth.now()
    private val _eventChannel = Channel<MonthChannel>()
    val eventChannel = _eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(MonthCalendarState())
    val state = _state.asStateFlow()
    val dialogSettings = DialogSettings()

    private var currentPage = state.value.initialPage
    init {
        _state.value.calendarMonth = repository.getCalenderMonth(state.value.yearMonth)
    }
    fun onPager(newPageIndex: Int){
        println("...onPager($newPageIndex)")
        if( newPageIndex != currentPage){
            val numMonths: Int =  newPageIndex - currentPage
            println(" number of months to add $numMonths")
            currentPage = newPageIndex
            currentYearMonth = currentYearMonth.plusMonths(numMonths.toLong())
            _state.update { it.copy(
                    yearMonth = currentYearMonth,
                    calendarMonth = repository.getCalenderMonth(currentYearMonth)
                )
            }
        }
    }
    fun onEvent(event : MonthCalendarEvent){
        println("...onEvent ${event.toString()}")
        when(event){
            is MonthCalendarEvent.MonthYear -> {println("yearmonth changed")}
            is MonthCalendarEvent.Pager -> {onPager(event.page)}
            is MonthCalendarEvent.ShowAddItemDialog -> { //TODO deprecate
                println("please, show add item dialog: ${event.show}")
                _state.update {  it.copy(
                    showAddItemDialog = event.show
                )}
            }
            is MonthCalendarEvent.CalendarDateClick -> {
                println("on calendar date click ")
                if( event.calendarDate.hasEvents()){
                    println("go to day calendar")
                    _state.update { it.copy(
                        currentCalendarDate = event.calendarDate,
                        navigateToDate = true
                    ) }
                }else{
                    dialogSettings.targetDate = event.calendarDate.date
                    dialogSettings.isCalendarItem = true
                    viewModelScope.launch {
                        _eventChannel.send(MonthChannel.ShowAddItemDialog)
                    }
                    _state.update { it.copy(
                        showAddItemDialog = true,
                        currentCalendarDate = event.calendarDate
                    ) }
                }
            }
            is MonthCalendarEvent.InsertItem ->  {
                insertItem(event.item)
            }
        }
    }
    private fun insertItem(item: Item){
        println("...ViewModel.insertItem(Item) ${item.heading}")
        val itemWithId = repository.insert(item)
        _state.value.calendarMonth!!.addEvent(item)
        //_state.update { monthCalendarState ->  }
        //_state.value.calendarMonth.addEvent(item)
        _state.update { it.copy(
            //calendarMonth
            //calendarDates = CalenderWorker.getCalenderDates(currentYearMonth, context)
        ) }
    }
}