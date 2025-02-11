package se.curtrune.lucy.statistics

import se.curtrune.lucy.LucindaApplication
import java.time.LocalDate

class StatisticsPeriod(val from: LocalDate,val  to: LocalDate) {
    private val repository = LucindaApplication.repository
    lateinit var statistics: Statistics
    init {
        val items = repository.selectItems(from, to)
        statistics = Statistics(items)
    }
}