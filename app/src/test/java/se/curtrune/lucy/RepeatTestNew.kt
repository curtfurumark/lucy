package se.curtrune.lucy

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
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
        val item = Item("bålövningar")
        println("firstDate: $firstDate, lastDate: $lastDate")
        val repeat = Repeat(
            weekDays = weekDays,
            firstDate = firstDate,
            lastDate = lastDate,
            isInfinite = false
        )
        item.repeat = repeat
        item.repeatID = 42
        val items = Repeater.createInstances(item)
        items.forEach{ instance ->
            println("heading: ${instance.heading}, dayOfWeek: ${instance.targetDate.dayOfWeek}, repeatID: ${instance.repeatID}")
        }
        assertEquals(2, items.size )

    }
    @Test
    fun testToJson(){
        println("testToJson()")
/*        val weekDays = listOf<DayOfWeek>(DayOfWeek.TUESDAY, DayOfWeek.SATURDAY)
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
        assertEquals(expectJson, json)*/
    }
    @Test
    fun testFromJson(){
        println("testFromJson()")
/*        val expectJson = "{\"templateID\":-1,\"unit\":\"DAY\",\"qualifier\":1,\"infinite\":false,\"firstDate\":\"2025-02-24\",\"lastDate\":\"2025-03-03\",\"weekDays\":[\"TUESDAY\",\"SATURDAY\"]}"
        val repeatSpec = MyGson.getMyGson().fromJson(expectJson, RepeatKt::class.java)
        assertNotNull(repeatSpec)
        println("isInfinite: ${repeatSpec.infinite}")
        println("firstDate: ${repeatSpec.firstDate}")
        println("weekDays: ${repeatSpec.weekDays}")*/
    }
    @Test
    fun repeaterBasicEveryDayForAWeek(){
        println("repeat every day for a week")
        val repeat = Repeat(
            firstDate = LocalDate.of(2025, 3, 3),
            lastDate = LocalDate.of(2025, 3, 9),
            isInfinite = false,
            qualifier = 1,
            unit = Repeat.Unit.DAY,
        )
        val item = Item("one week, every day")
        item.repeat = repeat
        val repeater = Repeater
        val items = repeater.createInstances(item)
        items.forEach{ item->
            println(item.toString())
        }
        assertEquals(7, items.size)
    }
    @Test
    fun repeaterInfiniteEveryDayTest(){
        println("infinite repeat every day")
        val infiniteRepeat = Repeat(
            firstDate = LocalDate.of(2025, 3, 3),
            lastDate = LocalDate.of(2025, 3, 9),//should be ignored
            isInfinite = true,
            qualifier = 1,
            unit = Repeat.Unit.DAY,
            templateID = 0
        )
        val item = Item("borsta tänderna")
        item.repeat = infiniteRepeat
        val repeater = Repeater
        //val lastInfiniteDay = repeater.lastInfiniteDate
        val items = repeater.createInstances(item)
        //val items = repeater.getItems(infiniteRepeat)
        println("number of items: ${items.size}")
        items.forEach{ instance->
            println(instance.toString())
        }
        assertEquals(32, items.size)
    }
}




