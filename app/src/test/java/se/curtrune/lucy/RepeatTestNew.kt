package se.curtrune.lucy

import org.junit.Assert.assertEquals
import org.junit.Test
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.RepeatKt
import se.curtrune.lucy.persist.RepeatItems
import se.curtrune.lucy.persist.Repeater
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
        val items = Repeater.getItems(repeat)
        items.forEach{ item ->
            println("heading: ${item.heading}, dayOfWeek: ${item.targetDate.dayOfWeek}")
        }
        assertEquals(2, items.size )

    }
    @Test
    fun repeaterBasicEveryDayForAWeek(){
        println("repeat every day for a week")
        val basicRepeat = RepeatItems.BasicRepeat(
            firstDate = LocalDate.of(2025, 3, 3),
            lastDate = LocalDate.of(2025, 3, 9),
            isInfinite = false,
            qualifier = 1,
            unit = Repeater.Unit.DAY,
            template = Item("repeat me mon to sun")
        )
        val repeater = Repeater
        val items = repeater.getItems(repeat =basicRepeat)
        items.forEach{ item->
            println(item.toString())
        }
        assertEquals(7, items.size)
    }
    @Test
    fun repeaterInfiniteEveryDayTest(){
        println("infinite repeat every day")
        val infiniteRepeat = RepeatItems.BasicRepeat(
            firstDate = LocalDate.of(2025, 3, 3),
            lastDate = LocalDate.of(2025, 3, 9),//should be ignored
            isInfinite = true,
            qualifier = 1,
            unit = Repeater.Unit.DAY,
            template = Item("repeat me mon to sun")
        )
        val repeater = Repeater
        val lastInfiniteDay = repeater.lastInfiniteDate
        val items = repeater.getItems(infiniteRepeat)
        items.forEach{ item->
            println(item.toString())
        }
        assertEquals(32, items.size)
    }
}




