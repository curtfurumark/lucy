package se.curtrune.lucy.screens.monthcalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.modules.LucindaApplication
import se.curtrune.lucy.activities.kotlin.composables.DialogSettings
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.modules.MainModule
import java.time.YearMonth

class MonthViewModel: ViewModel() {
    private val repository = LucindaApplication.appModule.repository
    private var currentYearMonth = YearMonth.now()
    private val _eventChannel = Channel<MonthChannel>()
    val eventChannel = _eventChannel.receiveAsFlow()
    private val _state = MutableStateFlow(MonthCalendarState())
    val state = _state.asStateFlow()
    val dialogSettings = DialogSettings()
        //private set

    private var currentPage = state.value.initialPage
    init {
        _state.value.calendarMonth = repository.getCalenderMonth(state.value.yearMonth)
        MainModule.setTitle(currentYearMonth)
    }
    fun onPager(newPageIndex: Int){
        println("...onPager($newPageIndex)")
        if( newPageIndex != currentPage){
            val numMonths: Int =  newPageIndex - currentPage
            println(" number of months to add $numMonths")
            currentPage = newPageIndex
            currentYearMonth = currentYearMonth.plusMonths(numMonths.toLong())
            setYearMonth(currentYearMonth)
        }
    }
    fun onEvent(event : MonthCalendarEvent){
        println("...onEvent ${event.toString()}")
        when(event){
            is MonthCalendarEvent.MonthYear -> {println("yearmonth changed")}
            is MonthCalendarEvent.Pager -> {onPager(event.page)}
            is MonthCalendarEvent.CalendarDateClick -> {
                println("on calendar date click ")
                if( event.calendarDate.hasEvents()){
                    println("go to day calendar")
                    _state.update { it.copy(
                        currentCalendarDate = event.calendarDate,
                    ) }
                    viewModelScope.launch {
                        _eventChannel.send(MonthChannel.NavigateToDayCalendar)
                    }
                }else{
                    dialogSettings.targetDate = event.calendarDate.date
                    dialogSettings.isCalendarItem = true
                    viewModelScope.launch {
                        _eventChannel.send(MonthChannel.ShowAddItemDialog)
                    }
                }
            }
            is MonthCalendarEvent.InsertItem ->  {
                insertItem(event.item)
            }

            MonthCalendarEvent.ShowYearMonthDialog -> {
                showYearMonthDialog()
            }
        }
    }
    private fun setYearMonth(yearMonth: YearMonth){
        println("setYearMonth(${yearMonth.toString()})")
        _state.update {
            it.copy(
                yearMonth = currentYearMonth,
                calendarMonth = repository.getCalenderMonth(currentYearMonth)
            )
        }
        MainModule.setTitle(currentYearMonth)
    }
    private fun showYearMonthDialog(){
        println("show yearMonth dialog")

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