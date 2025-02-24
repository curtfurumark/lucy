package se.curtrune.lucy

import org.junit.Assert.assertEquals
import org.junit.Test
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.RepeatKt
import java.time.DayOfWeek
import java.time.LocalDate

class RepeatTestNew {
    @Test
    fun repeatTueSat(){
        //from mon to mon
        println("repeatTueSat")
        val weekDays = listOf<DayOfWeek>(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY)
        val firstDate = LocalDate.of(2025, 2, 24)
        val lastDate = firstDate.plusWeeks(1)
        println("firstDate: $firstDate, lastDate: $lastDate")
        val repeat = RepeatItems.RepeatWeekDays(
            item = Item("test item"),
            weekDays = weekDays,
            firstDate = firstDate,
            lastDate = lastDate,
            isInfinite = false
        )
        val items = Repeater.getDates(repeat)
        items.forEach{ item ->
            println("heading: ${item.heading}, dayOfWeek: ${item.targetDate.dayOfWeek}")
        }
        assertEquals(2, items.size )

    }
}

sealed interface RepeatItems{
    //var lastActualDate: LocalDate
    data class BasicRepeat(val firstDate: LocalDate, val lastDate: LocalDate): RepeatItems {
    }

    data class RepeatWeekDays(
        val item: Item,
        val weekDays: List<DayOfWeek>,
        val firstDate: LocalDate,
        val lastDate: LocalDate,
        var lastActualDate: LocalDate? = null,
        val isInfinite: Boolean): RepeatItems {
    }
}

object Repeater{
    fun getDates(repeat: RepeatItems): List<Item>{
        return when(repeat){
            is RepeatItems.BasicRepeat -> {
                getDates(repeat)
            }
            is RepeatItems.RepeatWeekDays -> {
                getDates(repeat)
            }
        }
    }
    private fun getDates(repeat: RepeatItems.BasicRepeat): List<Item>{
        val dates: MutableList<Item> = mutableListOf()
        return dates
    }
    private fun getDates(repeatWeekDays: RepeatItems.RepeatWeekDays): List<Item>{
        println("getDates()")
        val items: MutableList<Item> = mutableListOf<Item>()
        var currentDate = repeatWeekDays.firstDate
        val lastDate = if( repeatWeekDays.isInfinite) repeatWeekDays.firstDate.plusMonths(1) else repeatWeekDays.lastDate
        if( repeatWeekDays.isInfinite){
            repeatWeekDays.lastActualDate = lastDate
        }
        while (currentDate.isBefore(lastDate)){
            repeatWeekDays.weekDays.forEach{ dayOfWeek ->
                if( currentDate.dayOfWeek.equals(dayOfWeek)){
                    val item = Item(repeatWeekDays.item)
                    item.targetDate = currentDate
                    items.add(item)
                }
            }
            currentDate = currentDate.plusDays(1)
        }
        return items
    }
}