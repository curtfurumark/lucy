package se.curtrune.lucy.statistics

import se.curtrune.lucy.app.LucindaApplication
import java.time.LocalDate
import se.curtrune.lucy.classes.State

class StatisticsPeriod(var from: LocalDate, var to: LocalDate, state: State = State.DONE) {
    private val repository = LucindaApplication.appModule.repository
    var statistics: Statistics
    init {
        val items = repository.selectItems(from, to, state = state)
        statistics = Statistics(items)
    }
    companion object{
        enum class Period{
            DAY, WEEK, MONTH, YEAR
        }
    }
}