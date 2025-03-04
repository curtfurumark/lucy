package se.curtrune.lucy.screens.week_calendar

import android.content.Context
import androidx.lifecycle.MutableLiveData
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
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.persist.CalenderWorker
import se.curtrune.lucy.screens.daycalendar.DayCalendarState
import se.curtrune.lucy.screens.daycalendar.DayChannel
import java.time.LocalDate

class WeekViewModel: ViewModel() {
    private val eventChannel = Channel<WeekChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    private val repository = LucindaApplication.repository
    private val _state = MutableStateFlow(WeekState())
    val state = _state.asStateFlow()
    private var week = Week()
    //val pagerState = MutableStateFlow(PagerState())
    val pagerState = PagerState()
    var mutableWeek: MutableLiveData<Week> = MutableLiveData()
    //var initialPage: Int = 5
    var currentPage = pagerState.initialPage
    var dialogSettings = DialogSettings()
    init{
        println("init week view model with context")
        mutableWeek.value = week
        _state.update { it.copy(
            currentWeek = Week(),
            currentParent = repository.getTodoRoot(),
            calendarDates = repository.getEvents(it.currentWeek)
        ) }

    }
    private fun addItem(item: Item){
        println("addItem(${item.heading})")
        repository.insert(item)
        _state.update { it.copy(
            calendarDates = repository.getEvents(it.currentWeek)
        ) }
    }
    private fun calendarDateClick(calenderDate: CalenderDate){
        println("calendarDateClick()")
        if( calenderDate.items.isEmpty()){
            println("show add item dialog")
            dialogSettings = DialogSettings()
            dialogSettings.isCalendarItem = true
            dialogSettings.targetDate = calenderDate.date
            dialogSettings.parent = repository.getTodoRoot()
            viewModelScope.launch {
                eventChannel.send(WeekChannel.ShowAddItemDialog(calenderDate.date))
            }
        }else{
            println("go to day calendar")
            viewModelScope.launch {
                eventChannel.send(WeekChannel.ViewDay(calenderDate))
            }
        }

    }
    fun onEvent(event: WeekEvent){
        println("WeekViewModel.onEvent(WeekEvent)")
        when(event){
            is WeekEvent.CalendarDateClick -> {calendarDateClick(event.calendarDate)}
            is WeekEvent.Week -> {}
            is WeekEvent.OnPage -> {onPage(event.page)}
            is WeekEvent.ShowAddItemDialog -> showAddItemDialog()
            is WeekEvent.AddItem -> {addItem(event.item)}
        }
    }
    private fun onPage(newPageIndex: Int){
        println("...onPage($newPageIndex)")
        val numWeeks: Int =  newPageIndex - currentPage
        println(" number of weeks to add $numWeeks")
        currentPage = newPageIndex
        week = week.plusWeek(numWeeks)
        _state.update { it.copy(
            currentWeek = week,
            calendarDates = repository.getEvents(week)
        ) }
    }
    private fun showAddItemDialog(){
        dialogSettings = DialogSettings()
        dialogSettings.isCalendarItem = true
        dialogSettings.parent = repository.getTodoRoot()
        viewModelScope.launch {
            eventChannel.send(WeekChannel.ShowAddItemDialog(LocalDate.now()))
        }
    }
}