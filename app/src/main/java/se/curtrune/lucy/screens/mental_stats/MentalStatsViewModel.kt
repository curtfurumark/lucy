package se.curtrune.lucy.screens.mental_stats

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import se.curtrune.lucy.statistics.StatisticsPeriod
import java.time.LocalDate

class MentalStatsViewModel: ViewModel() {
    private val _state = MutableStateFlow(MentalStatsState())
    val state = _state.asStateFlow()
    init {
        val statistics = StatisticsPeriod(from = LocalDate.now(), to = LocalDate.now())
    }

    fun onEvent(event: MentalStatsEvent){
        when(event){
            is MentalStatsEvent.Period -> {setPeriod(event.period)}
        }
    }
    private fun setPeriod(period: StatisticsPeriod.Companion.Period){
        println("...setPeriod(${period.toString()})")

    }
}