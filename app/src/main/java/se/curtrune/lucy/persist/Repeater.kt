package se.curtrune.lucy.persist

import android.content.Context
import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.item.Repeat
import se.curtrune.lucy.util.Logger
import se.curtrune.lucy.workers.RepeatWorker
import java.time.LocalDate

object Repeater{
    val lastInfiniteDate: LocalDate = LocalDate.now().plusMonths(1)
    enum class Unit{
        DAY, WEEK, MONTH, YEAR
    }
    fun getItems(repeat: Repeat): List<Item>{
        return emptyList()
    }
    fun createInstances(item: Item):List<Item>{
        println("Repeater.getInstances()")
        if( !item.hasRepeat()){
            println("WARNING, trying to get instances from item with no repeat")
            return emptyList()
        }
        if( item.repeat.weekDays.isNotEmpty()){
            return createInstancesWeekDays(item)
        }
        val repeat = item.repeat
        val items: MutableList<Item> = mutableListOf()
        var currentDate = repeat.firstDate?: return emptyList()

        val lastDate = if( repeat.isInfinite) repeat.firstDate?.plusMonths(1) else repeat.lastDate
        while(currentDate.isBefore(lastDate) || currentDate == lastDate){
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

    /**
     * @param template, the template to use for creating instances
     * @param context, context
     * @return the saved item with id
     */
    fun insertRepeat(template: Item, context: Context?): Item {
        Logger.log("ItemsWorker.insertRepeat(Item, Context)", template.heading)
        return RepeatWorker.insertItemWithRepeat(template, context)
    }
/*    private fun getItems(repeat: Repeat): List<Item>{
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
    private fun createInstancesWeekDays(item: Item): List<Item>{
        println("createInstancesWeekDays(${item.heading})")
        val items: MutableList<Item> = mutableListOf<Item>()
        var currentDate = item.repeat.firstDate
        val lastDate = if( item.repeat.isInfinite) item.repeat.firstDate.plusMonths(1) else item.repeat.lastDate
        while (currentDate.isBefore(lastDate)){
            item.repeat.weekDays.forEach{ dayOfWeek ->
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



