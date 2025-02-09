package se.curtrune.lucy.statistics

import se.curtrune.lucy.classes.Item

class Statistics(var items: List<Item>) {
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
            return  items.map { item->item.duration }.sum()
        }
    val anxiety: Int
        get(){
            return items.map { item -> item.anxiety }.sum()
        }
    val energy: Int
        get() {
            return items.map { item -> item.energy }.sum()
        }
    val mood: Int
        get(){
            return items.map { item -> item.mood }.sum()
        }
    val stress: Int
        get(){
            return items.map { item->item.stress }.sum()
        }
    val groupedByCategory: HashMap<String, MutableList<Item>>
        get(){
            val map = HashMap<String,MutableList<Item>>()
            items.forEach{ item ->
                map.getOrPut(item.category){ mutableListOf<Item>()}.add(item)
            }
            return map
        }

}