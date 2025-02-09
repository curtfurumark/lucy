package se.curtrune.lucy.statistics

import se.curtrune.lucy.classes.Item

class StatisticsByCategory(val items: List<Item>) {
    private val  categoryMap = HashMap<String,MutableList<Item>>()
    init {
        groupByCategory()
    }
    fun getCategories(): List<String>{
        return categoryMap.keys.toList()
    }
    fun getItems(category: String): List<Item>{
        return categoryMap[category]?.toList() ?: emptyList()
    }
    fun getStatisticsForCategory(category: String): Statistics{
        return Statistics(getItems(category))
    }
    private fun groupByCategory(){
        items.forEach{ item ->
            categoryMap.getOrPut(item.category){ mutableListOf<Item>()}.add(item)
        }
    }
}