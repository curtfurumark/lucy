package se.curtrune.lucy.screens.mental_stats

import androidx.lifecycle.ViewModel
import se.curtrune.lucy.statistics.StatisticsPeriod

class MentalStatsViewModel: ViewModel() {

    fun onEvent(event: MentalStatsEvent){
        when(event){
            is MentalStatsEvent.Period -> {setPeriod(event.period)}
        }
    }
    private fun setPeriod(period: StatisticsPeriod.Companion.Period){
        println("...setPeriod(${period.toString()})")

    }
}