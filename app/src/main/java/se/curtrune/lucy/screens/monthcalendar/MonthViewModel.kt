package se.curtrune.lucy.screens.monthcalendar

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.persist.CalenderWorker
import se.curtrune.lucy.persist.ItemsWorker
import java.time.YearMonth

class MonthViewModel(private val context: Context): ViewModel() {
    private var currentYearMonth = YearMonth.now()
    var initialPage = 5
    var numPages = 10
    private val _state = MutableStateFlow(MonthCalendarState())
    val state = _state.asStateFlow()

    private var currentPage = initialPage
    //private var _calendarDates = MutableLiveData<List<CalenderDate>>()
    //var yearMonth : MutableLiveData<YearMonth> = MutableLiveData(YearMonth.now())
    init {
        //_state.value.calendarDates = CalenderWorker.getCalenderDates(state.value.yearMonth, context)
        _state.value.calendarMonth = CalenderWorker.getCalenderMonth(state.value.yearMonth, context)
    }
    fun onPager(newPageIndex: Int){
        println("...onPager $newPageIndex")
        if( newPageIndex != currentPage){
            val numMonths: Int =  newPageIndex - currentPage
            println(" number of months to add $numMonths")
            currentPage = newPageIndex
            currentYearMonth = currentYearMonth.plusMonths(numMonths.toLong())
            val calendarMonth = CalenderWorker.getCalenderMonth(currentYearMonth, context)
            _state.update { it.copy(
                    yearMonth = currentYearMonth,
                    //calendarDates = CalenderWorker.getCalenderDates(currentYearMonth, context)
                    calendarMonth = CalenderWorker.getCalenderMonth(currentYearMonth, context)
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
                    _state.update { it.copy(
                        showAddItemDialog = true,
                        currentCalendarDate = event.calendarDate
                    ) }
                }
            }
            is MonthCalendarEvent.InsertItem ->  {
                insertItem(event.item)
                //println("insert item: ${event.item}")
            }
        }
    }
    private fun insertItem(item: Item){
        println("...ViewModel.insertItem(Item) ${item.heading}")
        val itemWithId = ItemsWorker.insert(item, context)
        _state.value.calendarMonth!!.addEvent(item)
        //_state.update { monthCalendarState ->  }
        //_state.value.calendarMonth.addEvent(item)
        _state.update { it.copy(
            //calendarMonth
            //calendarDates = CalenderWorker.getCalenderDates(currentYearMonth, context)
        ) }
    }
}