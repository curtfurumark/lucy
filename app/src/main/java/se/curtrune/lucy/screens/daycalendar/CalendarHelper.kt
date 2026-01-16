package se.curtrune.lucy.screens.daycalendar

import se.curtrune.lucy.classes.item.Item

object CalendarHelper {
    fun getCategorizedItems(): List<ItemsWithHeader> {
        return emptyList()
    }
    fun getItemsCategorized(categories: List<String>, items: List<Item>): Map<String, List<Item>> {
        val categorizedItems = mutableMapOf<String, MutableList<Item>>()
        categories.forEach { category ->
            categorizedItems[category] = mutableListOf()
            items.forEach { item ->
                if (item.category == category) {
                    categorizedItems[category]?.add(item)
                }
            }
        }
        return categorizedItems
    }
    fun getTodoToday(items: List<Item>): List<Item> {
        return items.filter { item -> item.targetTimeSecondOfDay == 0 }
    }
    fun getTimedItems(items: List<Item>): List<Item> {
        return items.filter { item -> item.targetTimeSecondOfDay != 0 }
    }
    fun getTodoTomorrow(){

    }
    fun getItems(): List<Item> {
        return emptyList()
    }
}


data class ItemsWithHeader(
    val name: String,
    val items: List<Item>
)