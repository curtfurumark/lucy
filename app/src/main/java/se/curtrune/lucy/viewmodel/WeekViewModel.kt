package se.curtrune.lucy.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.CalenderWorker

class WeekViewModel(val context: Context): ViewModel() {
    private var week = Week()
    var mutableWeek: MutableLiveData<Week> = MutableLiveData()
    var calendarDates: MutableLiveData<List<CalenderDate>> = MutableLiveData()
    var initialPage: Int = 5
    var currentPage = initialPage
    var numPages: Int = 10
    init{
        println("init week view model with context")
        mutableWeek.value = week
        val dates = CalenderWorker.getEvents(week, context)
        Logger.log("\t\tnumber of dates", dates.size)
        calendarDates.value = dates
    }
    fun onPage(newPageIndex: Int){
        println("...onPage($newPageIndex)")
        val numWeeks: Int =  newPageIndex - currentPage
        println(" number of weeks to add $numWeeks")
        currentPage = newPageIndex
        week = week.plusWeek(numWeeks)
        mutableWeek.value = week
        calendarDates.value = CalenderWorker.getEvents(week, context)
    }
}