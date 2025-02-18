package se.curtrune.lucy.screens.mental_stats

import se.curtrune.lucy.statistics.StatisticsPeriod

data class MentalStatsState(
    val period: StatisticsPeriod.Companion.Period = StatisticsPeriod.Companion.Period.DAY
)