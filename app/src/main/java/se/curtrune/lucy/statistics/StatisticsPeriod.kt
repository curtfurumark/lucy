package se.curtrune.lucy.statistics

import se.curtrune.lucy.LucindaApplication
import java.time.LocalDate

class StatisticsPeriod(var from: LocalDate, var to: LocalDate) {
/*    constructor(period: Period) : this() {
        val firstDate = when(period){
            Period.DAY -> {LocalDate.now()}
            Period.WEEK -> {LocalDate.now().minusWeeks(1)}
            Period.MONTH -> {LocalDate.now().minusMonths(1)}
            Period.YEAR -> {LocalDate.now().minusYears(1)}
        }
        this.from = firstDate
        this.to = LocalDate.now()
    }*/
    private val repository = LucindaApplication.repository
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