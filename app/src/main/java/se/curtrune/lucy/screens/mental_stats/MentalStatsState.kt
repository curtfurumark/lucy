package se.curtrune.lucy.screens.mental_stats

import se.curtrune.lucy.statistics.StatisticsPeriod
import java.time.LocalDate

data class MentalStatsState(
    val firstDate: LocalDate = LocalDate.now(),
    val lastDate: LocalDate = LocalDate.now(),
    val mentalDates: List<MentalDate> = emptyList(),
    val period: StatisticsPeriod.Companion.Period = StatisticsPeriod.Companion.Period.WEEK,

    val sumEnergy: Int = 0,
    val sumMood: Int = 0,
    val sumStress: Int = 0,
    val sumAnxiety: Int = 0
    //val period: StatisticsPeriod.Companion.Period = StatisticsPeriod.Companion.Period.DAY,
    //val stats:StatisticsPeriod =  StatisticsPeriod(from = LocalDate.now(), to = LocalDate.now())
)