package se.curtrune.lucy.activities.kotlin.monthcalendar

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.workers.CalenderWorker
import java.time.YearMonth

class MonthViewModel(private val context: Context): ViewModel() {
    private var currentYearMonth = YearMonth.now()
    var initialPage = 5
    var numPages = 10
    private val _state = MutableStateFlow(MonthCalendarState())
    val state = _state.asStateFlow()

    private var currentPage = initialPage
    private var _calendarDates = MutableLiveData<List<CalenderDate>>()
    //var yearMonth : MutableLiveData<YearMonth> = MutableLiveData(YearMonth.now())
    init {
        _state.value.calendarDates = CalenderWorker.getCalenderDates(state.value.yearMonth, context)
    }
    fun onPager(newPageIndex: Int){
        println("...onPager $newPageIndex")
        if( newPageIndex != currentPage){
            val numMonths: Int =  newPageIndex - currentPage
            println(" number of months to add $numMonths")
            currentPage = newPageIndex
            currentYearMonth = currentYearMonth.plusMonths(numMonths.toLong())
            _state.update { it.copy(
                    yearMonth = currentYearMonth,
                    calendarDates = CalenderWorker.getCalenderDates(currentYearMonth, context)
                )
            }
/*            _state.update { it.copy(

            ) }*/
        }
    }
    fun onEvent(event : MonthCalendarEvent){
        println("...onEvent ${event.toString()}")
        when(event){
            is MonthCalendarEvent.MonthYear -> {println("yearmonth changed")}
            is MonthCalendarEvent.Pager -> {onPager(event.page)}
            is MonthCalendarEvent.ShowAddItemDialog -> {
                println("please, show add item dialog: ${event.show}")
                _state.update {  it.copy(
                    showAddItemDialog = event.show
                )}
            }
            is MonthCalendarEvent.CalendarDateClick -> {
                println("on calendar date click ")
                if( event.calendarDate.hasEvents()){
                    println("go to day calendar")
                }else{
                    _state.update { it.copy(
                        showAddItemDialog = true
                    ) }
                }
            }
            is MonthCalendarEvent.InsertItem ->  {
                println("insert item: ${event.item}")
            }
        }
    }
}