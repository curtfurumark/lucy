package se.curtrune.lucy.screens.duration.data

import se.curtrune.lucy.classes.item.Item
import java.time.LocalDate

data class DateStatistics(
    val date: LocalDate,
    val items: List<Item>
)

sealed interface StatisticItem{
    fun duration(): Long
    data class Date(val date: LocalDate, val items: List<Item>): StatisticItem {
        override fun duration(): Long {
            return items.sumOf { it.duration }
        }
    }

    data class Category(val category: String, val items: List<Item>): StatisticItem {
        override fun duration(): Long {
            return items.sumOf { it.duration }
        }
    }

    data class Tag(val tag: String, val items: List<Item>): StatisticItem {
        override fun duration(): Long {
            return items.sumOf { it.duration }
        }
    }
}
