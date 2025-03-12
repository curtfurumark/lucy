package se.curtrune.lucy.persist

import android.content.Context
import se.curtrune.lucy.LucindaApplication
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.calender.CalendarWeek
import se.curtrune.lucy.classes.calender.CalenderDate
import se.curtrune.lucy.classes.calender.Week
import se.curtrune.lucy.util.Logger
import java.time.LocalDate

object CalendarHelper {
    private val repository = LucindaApplication.repository
    fun getCalendarWeek(week: Week): CalendarWeek {
        val items = repository.selectItems(week)
        val (allWeek, rest) = items.partition { item->item.itemDuration != null }
        val calendarDates = getCalendarDates(week, rest)
        val calendarWeek = CalendarWeek(week = week, calendarDates = calendarDates, allWeekItems =  allWeek)
        return calendarWeek
    }
    fun getCalendarDates(week: Week, items: List<Item>): List<CalenderDate>{
        val calenderDates: MutableList<CalenderDate> = ArrayList()
        var currentDate: LocalDate = week.firstDateOfWeek
        val lastDate = week.lastDateOfWeek
        while (currentDate.isBefore(lastDate) || currentDate == lastDate) {
            val calenderDate = CalenderDate(currentDate)
            val dateItems = items.filter { item: Item -> item.targetDate == currentDate }
            calenderDate.items = dateItems
            calenderDates.add(calenderDate)
            currentDate = currentDate.plusDays(1)
        }
        return calenderDates
    }
/*    fun getCalendarDates(week: Week): List<CalenderDate> {
        Logger.log("CalendarWorker.getEvents(Week, Context)", week.toString())
        repository.getCalendarWeek()
        return getCalenderDates(week.firstDateOfWeek, week.lastDateOfWeek)
    }*/

    /**
     * this one is supposed to return items to be shown in week or month calender
     * @param firstDate, i wonder
     * @param lastDate, your guess is as good as mine
     * @param context, i need context, we all need context, without context we are nothing
     * @return a list of appointments as calendarDates
     */
    fun getCalenderDates(
        firstDate: LocalDate,
        lastDate: LocalDate,
        context: Context?
    ): List<CalenderDate> {
        Logger.log("CalendarWorker.getCalenderDates(LocalDate, LocalDate, Context)")
        val calenderDates: MutableList<CalenderDate> = java.util.ArrayList()
        var currentDate = firstDate
        while (currentDate.isBefore(lastDate) || currentDate == lastDate) {
            val calenderDate = CalenderDate(currentDate)
            val items = selectCalenderItems(currentDate, context)
            calenderDate.items = items
            calenderDates.add(calenderDate)
            currentDate = currentDate.plusDays(1)
        }
        return calenderDates
    }
    fun selectCalenderItems(currentDate: LocalDate?, context: Context?): List<Item> {
        println("CalendarHelper.selectCalenderItems(LocalDate, Context)")
        val queery = Queeries.selectCalenderItems(currentDate)
        println("queery $queery")
        SqliteLocalDB(context).use { db ->
            return db.selectItems(queery)
        }
    }
}