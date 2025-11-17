package se.curtrune.lucy.screens.mental_stats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.curtrune.lucy.app.LucindaApplication
import se.curtrune.lucy.statistics.StatisticsPeriod
import java.time.LocalDate

class MentalStatsViewModel: ViewModel() {
    private val db = LucindaApplication.appModule.repository
    private val _state = MutableStateFlow(MentalStatsState())
    val state = _state.asStateFlow()
    init {
        //val firstDate = LocalDate.now().minusWeeks(1)
        val firstDate = LocalDate.now()
        val lastDate = LocalDate.now()
        val items = db.selectItems(firstDate, lastDate)
        val mentalStats = MentalStatsCalculator(items)
        val mentalDates = mentalStats.getMentalDates()
        _state.update { it.copy(
            period = StatisticsPeriod.Companion.Period.DAY,
            mentalDates = mentalDates,
            firstDate = firstDate,
            lastDate = lastDate)}
    }

    fun onEvent(event: MentalStatsEvent){
        when(event){
            is MentalStatsEvent.Period -> {setPeriod(event.period)}
        }
    }
    private fun setPeriod(period: StatisticsPeriod.Companion.Period){
        println("...setPeriod(${period.toString()})")
        var firstDate = LocalDate.now().minusWeeks(1)
        val lastDate = LocalDate.now()
        when(period){
            StatisticsPeriod.Companion.Period.DAY -> {
                firstDate = LocalDate.now()            }
            StatisticsPeriod.Companion.Period.WEEK -> {
                println("default")
            }
            StatisticsPeriod.Companion.Period.MONTH -> {
                firstDate = LocalDate.now().minusMonths(1)
            }
            StatisticsPeriod.Companion.Period.YEAR -> {
                firstDate = LocalDate.now().minusYears(1)
            }
        }
        val items = db.selectItems(firstDate, lastDate)
        val calculator = MentalStatsCalculator(items)
        val mentalDates = calculator.getMentalDates()
        mentalDates.forEach {
            println(it)
        }
        _state.update { it.copy(
            period = period,
            mentalDates = calculator.getMentalDates(),
            firstDate = firstDate,
            lastDate = lastDate
        ) }
    }
}