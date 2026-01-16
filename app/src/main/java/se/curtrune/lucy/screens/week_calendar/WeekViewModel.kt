package se.curtrune.lucy.screens.week_calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.classes.ItemDuration
import se.curtrune.lucy.classes.calender.CalendarDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.add_item.DefaultItemSettings
import se.curtrune.lucy.modules.TopAppbarModule

class WeekViewModel: ViewModel() {
    private val eventChannel = Channel<WeekChannel>()
    val eventFlow = eventChannel.receiveAsFlow()
    private val repository = LucindaApplication.appModule.repository
    private val _state = MutableStateFlow(WeekState())
    val state = _state.asStateFlow()
    private var week = Week()
    val pagerState = PagerState()
    var mutableWeek: MutableLiveData<Week> = MutableLiveData()
    var currentPage = pagerState.initialPage
    var defaultItemSettings = DefaultItemSettings()
    init{
        println("WeekViewModel.init")
        mutableWeek.value = week
        _state.update { it.copy(
            currentWeek = Week(),
            calendarWeek = repository.getCalendarWeek(it.currentWeek),
            currentParent = repository.getTodoRoot(),
        ) }
        TopAppbarModule.setTitle(state.value.currentWeek)

    }
    private fun addItem(item: Item){
        println("addItem(${item.heading})")
        repository.insert(item)
        _state.update { it.copy(
            calendarWeek = repository.getCalendarWeek(it.currentWeek)
        ) }
    }
    private fun calendarDateClick(calenderDate: CalendarDate){
        println("calendarDateClick($calenderDate.date.toString())")
        if( calenderDate.events.isEmpty()){
            println("show add item dialog")
            defaultItemSettings = defaultItemSettings.copy(
                item = Item().also { item->
                    item.targetDate = calenderDate.date
                    item.parent = state.value.currentParent
                    item.isCalenderItem = true
                }
            )
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
            is WeekEvent.OnAllWeekClick -> {onAllWeekClick(event.week)}
            is WeekEvent.OnPage -> {onPage(event.page)}
            is WeekEvent.ShowAddItemDialog -> {
                //showAddItemDialog()
                println("showAddItemDialog, probably redundant")
            }
            is WeekEvent.AddItem -> {addItem(event.item)}
            is WeekEvent.OnAllWeekLongClick -> {
                onAllWeekLongClick(event.week)
            }
            is WeekEvent.CalendarDateLongClick -> { calendarDateLongClick(event.calendarDate) }
            is WeekEvent.AddAllWeekItem -> {
                addAllWeekItem(event.item)
            }
        }
    }

    private fun addAllWeekItem(item: Item) {
        println("WeekViewModel.addAllWeekItem(${item.heading})")
        item.itemDuration = ItemDuration(ItemDuration.Type.WEEK)
        item.targetDate = state.value.currentWeek.firstDateOfWeek
        item.isCalenderItem = true
        if(repository.insert(item) == null){
            println("failed to insert item")
            return
        }
        _state.update { it.copy(
            calendarWeek = repository.getCalendarWeek(it.currentWeek)
        ) }

    }

    private fun calendarDateLongClick(calendarDate: CalendarDate) {
        println("WeekViewModel.calendarDateLongClick(${calendarDate.date.toString()})")
        viewModelScope.launch {
            //TODO use jetpack compose navigation 3
            //eventChannel.send(WeekChannel.Navigate(MyDayFragment(date = calendarDate.date)))
        }
    }

    private fun onAllWeekClick(week: Week){
        println("onAllWeekClick(Week)")
        viewModelScope.launch {
            eventChannel.send(
                WeekChannel.ShowAddAllWeekNote
            )
        }
    }
    private fun onAllWeekLongClick(week: Week){
        println("onAllWeekLongClick(${week.toString()})")
        viewModelScope.launch {
            eventChannel.send(WeekChannel.ShowAddAllWeekNote)
        }
    }
    private fun onPage(newPageIndex: Int){
        println("...onPage($newPageIndex)")
        if( newPageIndex == currentPage){
            println("no need to update week, new page same as old page")
            return
        }
        val numWeeks: Int =  newPageIndex - currentPage
        println(" number of weeks to add $numWeeks")
        currentPage = newPageIndex
        week = week.plusWeek(numWeeks)
        _state.update { it.copy(
            currentWeek = week,
            calendarWeek = repository.getCalendarWeek(week)
        ) }
        TopAppbarModule.setTitle(week)
    }
/*    private fun showAddItemDialog(){
        dialogSettings = DialogSettings()
        dialogSettings.isCalendarItem = true
        dialogSettings.parent = repository.getTodoRoot()
        viewModelScope.launch {
            eventChannel.send(WeekChannel.ShowAddItemDialog(LocalDate.now()))
        }
    }*/
}