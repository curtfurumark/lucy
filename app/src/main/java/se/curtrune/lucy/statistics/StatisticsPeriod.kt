package se.curtrune.lucy.statistics

import se.curtrune.lucy.modules.LucindaApplication
import java.time.LocalDate

class StatisticsPeriod(var from: LocalDate, var to: LocalDate) {
    private val repository = LucindaApplication.appModule.repository
    lateinit var statistics: Statistics
    init {
        val items = repository.selectItems(from, to)
        statistics = Statistics(items)
    }
    companion object{
        enum class Period{
            DAY, WEEK, MONTH, YEAR
        }
    }
}