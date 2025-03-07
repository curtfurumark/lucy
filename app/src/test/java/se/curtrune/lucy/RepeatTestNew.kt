package se.curtrune.lucy

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import se.curtrune.lucy.classes.Item
import se.curtrune.lucy.classes.RepeatKt
import se.curtrune.lucy.persist.RepeatItems
import se.curtrune.lucy.persist.Repeater
import se.curtrune.lucy.util.gson.MyGson
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
    fun testToJson(){
        println("testToJson()")
        val weekDays = listOf<DayOfWeek>(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY)
        val firstDate = LocalDate.of(2025, 2, 24)
        val lastDate = firstDate.plusWeeks(1)
        val repeatSpec = RepeatKt(
            unit = RepeatKt.Unit.DAY,
            qualifier = 1,
            infinite = false,
            firstDate =  firstDate,
            currentLastDate = null,
            lastDate = firstDate.plusWeeks(1),
            weekDays =  weekDays
        )
        val json = MyGson.getMyGson().toJson(repeatSpec)
        println(json)
        val expectJson = "{\"templateID\":-1,\"unit\":\"DAY\",\"qualifier\":1,\"infinite\":false,\"firstDate\":\"2025-02-24\",\"lastDate\":\"2025-03-03\",\"weekDays\":[\"TUESDAY\",\"SATURDAY\"]}"
        assertEquals(expectJson, json)
    }
    @Test
    fun testFromJson(){
        println("testFromJson()")
        val expectJson = "{\"templateID\":-1,\"unit\":\"DAY\",\"qualifier\":1,\"infinite\":false,\"firstDate\":\"2025-02-24\",\"lastDate\":\"2025-03-03\",\"weekDays\":[\"TUESDAY\",\"SATURDAY\"]}"
        val repeatSpec = MyGson.getMyGson().fromJson(expectJson, RepeatKt::class.java)
        assertNotNull(repeatSpec)
        println("isInfinite: ${repeatSpec.infinite}")
        println("firstDate: ${repeatSpec.firstDate}")
        println("weekDays: ${repeatSpec.weekDays}")
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




