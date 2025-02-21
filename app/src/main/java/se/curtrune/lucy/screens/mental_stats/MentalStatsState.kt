package se.curtrune.lucy.screens.mental_stats

import se.curtrune.lucy.statistics.StatisticsPeriod
import java.time.LocalDate

data class MentalStatsState(
    val period: StatisticsPeriod.Companion.Period = StatisticsPeriod.Companion.Period.DAY,
    val stats:StatisticsPeriod =  StatisticsPeriod(from = LocalDate.now(), to = LocalDate.now())
)