package se.curtrune.lucy.persist

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import java.time.DayOfWeek
import java.time.LocalDate

object Repeater{
    val lastInfiniteDate: LocalDate = LocalDate.now().plusMonths(1)
    enum class Unit{
        DAY, WEEK, MONTH, YEAR
    }
    fun getItems(repeat: Repeat): List<Item>{
        return emptyList()
    }
    fun getInstances(item: Item):List<Item>{
        return emptyList()
    }
/*    private fun getItems(repeat: RepeatItems.BasicRepeat): List<Item>{
        //val dates: MutableList<Item> = mutableListOf()
        println("Repeater.getItems()")
        val items: MutableList<Item> = mutableListOf<Item>()
        var currentDate = repeat.firstDate
        val lastDate = if( repeat.isInfinite) repeat.firstDate.plusMonths(1) else repeat.lastDate
        while(currentDate.isBefore(lastDate) || currentDate == lastDate){
            val actualItem = Item(repeat.template)
            actualItem.targetDate = currentDate
            items.add(actualItem)
            currentDate = when(repeat.unit){
                Unit.DAY -> currentDate.plusDays(repeat.qualifier)
                Unit.WEEK -> currentDate.plusWeeks(repeat.qualifier)
                Unit.MONTH -> {currentDate.plusMonths(repeat.qualifier)}
                Unit.YEAR -> {currentDate.plusYears(repeat.qualifier)}
            }

        }
        return items
    }*/
/*    private fun getItems(repeatWeekDays: RepeatItems.RepeatWeekDays): List<Item>{
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
    }*/
}



