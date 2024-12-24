package se.curtrune.lucy.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.workers.CalenderWorker
import se.curtrune.lucy.workers.ItemsWorker
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

class MonthViewModel(private val context: Context): ViewModel() {
    private var _yearMonth = YearMonth.now()
    var initialPage = 5
    var numPages = 10
    private var currentPage = initialPage
    private var calendarDates: MutableList<CalenderDate> = mutableListOf()
    var mutableCalendarDates =   MutableLiveData<List<CalenderDate>>()
    var yearMonth : MutableLiveData<YearMonth> = MutableLiveData(YearMonth.now())
    init {
        calendarDates = CalenderWorker.getCalenderDates(yearMonth.value, context)
        mutableCalendarDates.value = calendarDates
    }
    fun onPager(newPageIndex: Int){
        println("...onPager $newPageIndex")
        if( newPageIndex != currentPage){
            val numMonths: Int =  newPageIndex - currentPage
            println(" number of months to add $numMonths")
            currentPage = newPageIndex
            _yearMonth = _yearMonth.plusMonths(numMonths.toLong())
            yearMonth.value = _yearMonth
            calendarDates = CalenderWorker.getCalenderDates(_yearMonth, context)
            //calendarDates[1] = CalenderDate()
            mutableCalendarDates.value = CalenderWorker.getCalenderDates(_yearMonth, context)
        }

    }

    fun addEvent(item: Item) {
        println("MonthViewModel.add(Item) ${item.heading}")
        var insertedItem = ItemsWorker.insert(item, context)
        println(" item inserted ok with id ${insertedItem.id}")
        calendarDates.add(CalenderDate())
        //mutableCalendarDates.value?.plus(CalenderDate(item.targetDate, item))
        mutableCalendarDates.value = calendarDates
    }
}