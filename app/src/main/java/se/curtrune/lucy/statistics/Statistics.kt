package se.curtrune.lucy.statistics

import se.curtrune.lucy.classes.item.Item
import se.curtrune.lucy.classes.State
import se.curtrune.lucy.screens.duration.data.DateStatistics
import se.curtrune.lucy.screens.duration.data.StatisticItem
import java.time.LocalDate

class Statistics(var items: List<Item>) {
    val groupedByTags: List<StatisticItem.Tag>
        get() {
            return  emptyList()
        }
    val averageDuration: Long
        get(){
            if(items.isEmpty()) return 0
            return duration / items.size
        }
    val averageEnergy: Float
        get(){
            if(items.isEmpty()) return 0F
            return (energy.toFloat() / items.size)
        }
    val averageMood: Float
        get(){
            if(items.isEmpty()) return 0f
            return mood.toFloat() / items.size
        }
    val averageStress: Float
        get(){
            if(items.isEmpty()) return 0f
            return stress.toFloat()/ items.size
        }
    val duration: Long
        get(){
            return items.sumOf { item -> item.duration }
        }
    val durationActual: Long
        get(){
            return items.filter { item -> item.state.equals(State.DONE) }
                .sumOf { item -> item.duration }
        }
    val anxiety: Int
        get(){
            return items.sumOf { item -> item.anxiety }
        }
    val energy: Int
        get() {
            return items.sumOf { item -> item.energy }
        }
    val mood: Int
        get(){
            return items.sumOf { item -> item.mood }
        }
    val stress: Int
        get(){
            return items.sumOf { item -> item.stress }
        }
    val groupedByCategory: List<StatisticItem.Category>
        get(){
            val map = HashMap<String,MutableList<Item>>()
            val list = mutableListOf<StatisticItem.Category>()
            items.forEach{ item ->
                map.getOrPut(item.category){ mutableListOf<Item>()}.add(item)
            }
            map.forEach{ (category, items)->
                list.add(StatisticItem.Category(category, items))
            }
            return list
        }
    val groupedByDate: List<StatisticItem.Date>
        get(){
            val map = HashMap<LocalDate,MutableList<Item>>()
            val dateItems = mutableListOf<StatisticItem.Date>()
            items.forEach{ item->
                map.getOrPut(item.targetDate){ mutableListOf<Item>()}.add(item)
            }
            map.forEach{ (date, items)->
                dateItems.add(StatisticItem.Date(date, items))
            }
            return dateItems
        }
    private fun mapToDateStats(groupedByDate: HashMap<LocalDate, MutableList<Item>>): List<StatisticItem.Date> {
        val list = mutableListOf<StatisticItem.Date>()
        groupedByDate.forEach{ (date, items)->
            list.add(StatisticItem.Date(date, items))
        }
        return list
    }
}