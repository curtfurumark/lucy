package se.curtrune.lucy.persist

import android.content.Context
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import java.time.LocalDate

object Repeater{
    val lastInfiniteDate: LocalDate = LocalDate.now().plusMonths(1)
    private lateinit var repeat: Repeat

    enum class Unit{
        DAY, WEEK, MONTH, YEAR
    }
    fun getItems(repeat: Repeat): List<Item>{
        return emptyList()
    }

    /**
     * should only be called with a saved item,
     * children requires template id
     */
    fun createInstances(item: Item):List<Item>{
        println("Repeater.getInstances($item)")
        if( item.repeat == null){
            println("WARNING, trying to get instances from item with no repeat")
            return emptyList()
        }
        repeat = item.repeat!!

/*        if( item.repeat == null){
            println("WARNING, trying to get instances from item with null repeat")
            return emptyList()
        }*/
        println(repeat.toString())
        if(repeat.weekDays.isNotEmpty() ){
            return createInstancesWeekDays(item)
        }
        val items: MutableList<Item> = mutableListOf()
        var currentDate = repeat.firstDate
        val lastDate = getLastDate(repeat = repeat)
        while(currentDate.isBefore(lastDate)  || currentDate == lastDate){
            val instance = Item(item)
            instance.targetDate = currentDate
            items.add(instance)
            currentDate = when(repeat.unit){
                Repeat.Unit.DAY -> currentDate.plusDays(repeat.qualifier.toLong())
                Repeat.Unit.WEEK -> currentDate.plusWeeks(repeat.qualifier.toLong())
                Repeat.Unit.MONTH -> currentDate.plusMonths(repeat.qualifier.toLong())
                Repeat.Unit.YEAR -> currentDate.plusYears(repeat.qualifier.toLong())
            }
        }
        return items
    }
    private fun getLastDate(repeat: Repeat): LocalDate{
/*        if( repeat.lastDate != null){
            return repeat.lastDate!!
        }*/
        return repeat.firstDate.plusMonths(1)
    }

    private fun createInstancesWeekDays(item: Item): List<Item>{
        println("createInstancesWeekDays(${item.heading})")
        val items: MutableList<Item> = mutableListOf<Item>()
        //var currentDate = item.repeat?.firstDate
        if(item.repeat == null){
            println("WARNING, trying to get instances from item with null repeat")
            return emptyList()
        }

        var currentDate = repeat.firstDate
        println("currentDate: $currentDate")
        val lastDate = getLastDate(item.repeat!!)
        while (currentDate.isBefore(lastDate)){
            item.repeat?.weekDays?.forEach{ dayOfWeek ->
                if( currentDate.dayOfWeek.equals(dayOfWeek)){
                    val instance = Item(item)
                    instance.targetDate = currentDate
                    items.add(instance)
                }
            }
            currentDate = currentDate.plusDays(1)
        }
        return items
    }
}



