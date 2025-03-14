package se.curtrune.lucy

import org.junit.Test
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.composables.PostponeAmount
import se.curtrune.lucy.modules.PostponeWorker
import se.curtrune.lucy.util.Converter
import java.time.LocalDate
import java.time.LocalTime


class PostponeTest {

    @Test
    fun testPostpone(){
        println("testPostpone")
        val items = createItems()
        //printItems(items)
        postpone(from = LocalTime.of(12, 0, 0), minutes = 120, items = items)
        assert(items.size == 26)
    }
    @Test
    fun testPostponeWorker(){
        println("...testPostponeWorker()")
        var item = Item()
        item.targetTime = LocalTime.of(23, 30, 0)
        item.targetDate = LocalDate.of(2025, 1, 1)
        item = PostponeWorker.postponeItem(item, PostponeAmount.ONE_HOUR)
        assert(item.targetTime.equals(LocalTime.of(0, 30, 0)))
        assert(item.targetDate.equals(LocalDate.of(2025, 1, 2)))
    }
    private fun postpone(from: LocalTime, minutes: Long, items: List<Item>){
        println("postpone")
        val filteredItems = items.filter {item ->  item.targetTime.isAfter(from) }
        printItems(filteredItems)
        filteredItems.forEach{ item->
            item.targetTime = postpone(item ,minutes, from)
        }
        printItems(filteredItems)
    }
    private fun postpone(item: Item, minutes: Long, from: LocalTime): LocalTime{
        val newTime = item.targetTime.plusMinutes(minutes)
        println("new time ${newTime.toString()}")
        //if( item.targetTime.plusMinutes(minutes).isAfter(LocalTime.of(23, 59, 59))){
        /* if new targetTime is before from time, it means that targetTime has been pushed
        to beginning of day, not so much postponed as preponed, which we don't want
         */
        if( item.targetTime.plusMinutes(minutes).isBefore(from)){
            println("don't postpone to next day")
            return LocalTime.of(23, 59, 59)
        }
        return item.targetTime.plusMinutes(minutes)
    }

    private fun createItems(): List<Item>{
        println("...createItems")
        var items: MutableList<Item> = mutableListOf()
        var time = LocalTime.of(0, 10)
        for(i in 1..26){
            val item = Item("item $i")
            item.targetTime = time
            items.add(item)
            time = time.plusMinutes(55)
        }
        return items
    }
    private fun printItems(items : List<Item>){
        println("printItems")
        items.forEach{ item->
            println("${item.heading}, ${
                Converter.format(item.targetTime)}")
        }
    }
}